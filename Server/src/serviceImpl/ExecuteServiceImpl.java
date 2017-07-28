package serviceImpl;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Stack;

import service.ExecuteService;

public class ExecuteServiceImpl implements ExecuteService {
	private String code, input;
	private Stack<Integer> stack;
	String output;
	byte[] memory;
	int memoryPtr, codePtr, inputPtr;
	
	private void init(String code, String input) {
		this.code = code;
		this.input = input;
		int size = code.split(">").length;
		memory = new byte[size];
		Arrays.fill(memory, (byte) 0);
		stack = new Stack<>();
		memoryPtr = 0;
		codePtr = 0;
		inputPtr = 0;
		output = "";
	}
	
	void step() throws Exception {
		if (codePtr >= code.length())
			throw new Exception("END");
		switch (code.charAt(codePtr)) {
			case '>':
				memoryPtr++;
				break;
			case '<':
				if (memoryPtr > 0) memoryPtr--;
				break;
			case '+':
				memory[memoryPtr]++;
				break;
			case '-':
				memory[memoryPtr]--;
				break;
			case ',':
				if (inputPtr >= input.length())
					memory[memoryPtr] = 0;
				else 
					memory[memoryPtr] = (byte) input.charAt(inputPtr);
				inputPtr++;
				break;
			case '.':
				if (memory[memoryPtr] > 0) 
					output = output + Character.toString((char) memory[memoryPtr]);
				break;
			case '[':
				if (memory[memoryPtr] != 0)
					stack.push(codePtr);
				else 
					while (code.charAt(codePtr) != ']') codePtr++;
				break;
			case ']':
				if (memory[memoryPtr] != 0) 
					codePtr = stack.peek();
				else 
					stack.pop();
				break;
		}
		codePtr++;
	}
	
	void resume() throws Exception {
		long startTime = System.currentTimeMillis();
		while (codePtr < code.length()) {
			step();
			if (System.currentTimeMillis() - startTime > 10000) 
				throw new Exception("TIMEOUT");
		}
	}
	
	@Override
	public String execute(String code, String param) throws RemoteException {
		if (code.charAt(0) == 'O') {
			String newCode = "";
			for (int i = 0; i < code.length(); i += 10) {
				String command = code.substring(i, i + 9);
				if (command.equals("Ook. Ook?")) newCode += ">";
				if (command.equals("Ook? Ook.")) newCode += "<";
				if (command.equals("Ook. Ook.")) newCode += "+";
				if (command.equals("Ook! Ook!")) newCode += "-";
				if (command.equals("Ook! Ook.")) newCode += ".";
				if (command.equals("Ook. Ook!")) newCode += ",";
				if (command.equals("Ook! Ook?")) newCode += "[";
				if (command.equals("Ook? Ook!")) newCode += "]";			 
			}	
			code = newCode;
		}
		this.init(code, param);
		try {
			this.resume();
			return output;
		}
		catch (Exception e) {
			return "Error: " + e.getLocalizedMessage();
		}
	}
}