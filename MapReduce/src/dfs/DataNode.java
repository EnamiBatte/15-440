package dfs;

import java.io.*;
import java.net.*;
import java.util.*;

import util.*;

public class DataNode {
	
	public DataNode() {	
	}
	
	public int hash(String key, int num) {
		return key.hashCode() % num;
	}
	
	public ArrayList<String> createLocalInputPartition(String filename, int numberOfLines) throws Exception {
		System.out.println("Creating local partition");
		BufferedReader dr=new BufferedReader(new FileReader(filename));
		int lines = 0;
		String line = dr.readLine();
		while (!line.equals("")) {
			lines++;
			line = dr.readLine();
		}
		dr.close();
		System.out.println("The file had the following number of lines: "+String.valueOf(lines));
		int numberOfFiles = (lines - 1) / numberOfLines + 1;
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<BufferedWriter> dw = new ArrayList<BufferedWriter> ();
		for (int i = 0; i < numberOfFiles; i++) {
			String name = "m" + i + "_" + filename;
			files.add(name);
			File myFile = new File(name);
			myFile.createNewFile();
			dw.add(new BufferedWriter(new FileWriter(name)));
		}
		System.out.println("Files are successfully created");
		dr=new BufferedReader(new FileReader(filename));
		lines = 0;
		int i = 0;
		line = dr.readLine();
		System.out.println(line);
		while (!line.equals("")) {
			lines++;
			dw.get(i).write(line);
			dw.get(i).newLine();
			if (lines == numberOfLines) {
				i++;
				lines = 0;
			}
			line = dr.readLine();
			System.out.println(line);
		}
		while ((lines < numberOfLines) && (lines!=0)) {
			dw.get(i).newLine();
			lines++;
		}
		System.out.println("Files are successfully written");
		ArrayList<String> ret = new ArrayList<String>();
		for (String name : files) {
			ret.add(numberOfLines + "_" + name);
		}
		return ret;
	}
	public ArrayList<String> createLocalOutputPartition(String filename, int numberOfReducers) throws Exception {
		BufferedReader dr=new BufferedReader(new FileReader(filename));
		int[] lines = new int[numberOfReducers];
		String line = dr.readLine();
		while (!line.equals("")) {
			String key = line.split("|")[0];
			int i = hash(key, numberOfReducers);
			lines[i]++;
		}
		dr.close();
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<BufferedWriter> dw = new ArrayList<BufferedWriter> ();
		for (int i = 0; i < numberOfReducers; i++) {
			String name = "r" + filename+ "_" + i;
			files.add(name);
			File myFile = new File(name);
			myFile.createNewFile();
			dw.add(new BufferedWriter(new FileWriter(name)));
		}
		dr=new BufferedReader(new FileReader(filename));
		line = dr.readLine();
		while (!line.equals("")) {
			String key = line.split("|")[0];
			int i = hash(key, numberOfReducers);
			dw.get(i).write(line);
			dw.get(i).newLine();
		}
		ArrayList<String> ret = new ArrayList<String>();
		for (int i = 0; i < files.size(); i++) {
			ret.add(lines[i] + "_" + files.get(i));
		}
		return ret;
	}
	public int addFileToDFS(String filename, int Master_port, boolean flag) throws Exception {
		System.out.println("Adding File to DFS");
		//flag: true for map input, false for map output
		String masterIP = Configuration.Master_Address;
		Socket socket = new Socket(masterIP, Master_port);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		
		System.out.println("Socket Created");
		ArrayList<String> localPartition = new ArrayList<String>();
		if (flag) {
			int numberOfLines = Configuration.numberOfLines;
			localPartition = createLocalInputPartition(filename, numberOfLines);
		} else {
			int numberOfReducers = Configuration.numberOfReducers;
			localPartition = createLocalOutputPartition(filename, numberOfReducers);
		}
		System.out.println("Split Called");
		for (int i = 0; i < localPartition.size(); i++) {
			String a = localPartition.get(i);
			String arr[] = a.split("_", 2);
			int lines = Integer.parseInt(arr[0]);
			String partitionFilename = arr[1];
			Message message = new Message();
			message.setType('s');
			message.setFileName(partitionFilename);
			oos.writeObject(message);
			
			Message response = (Message)ois.readObject();
			ArrayList<String> addrList = response.getAddrList();
			
			for(String addr : addrList) {
				System.out.println("Addr:" + addr);
				int slaveport = Configuration.slaveListenPort;
				Socket socketDN = new Socket(addr, slaveport);
				InputStream isDN = socketDN.getInputStream();
				ObjectInputStream oisDN = new ObjectInputStream(isDN);
				OutputStream osDN = socketDN.getOutputStream();
				ObjectOutputStream oosDN = new ObjectOutputStream(osDN);
				
				message = new Message();
				message.setType('s');
				message.setFileName(partitionFilename);
				message.setLines(lines);
				oosDN.writeObject(message);
				writeFileToStream(partitionFilename, lines, osDN);
				
				Message msg = (Message)oisDN.readObject();
				socketDN.close();
			}
			
		}
		socket.close();
		return localPartition.size();
	}
	
	public void receiveFileFromStream(String filename, InputStream is, int lines) throws Exception {
		BufferedReader dr=new BufferedReader(new InputStreamReader(is));
		File myFile = new File(filename);
		myFile.createNewFile();
		BufferedWriter dw = new BufferedWriter(new FileWriter(filename));
		for (int i = 0; i < lines; i++) {
			dw.write(dr.readLine());
			dw.newLine();
		} 
		dr.close();
		dw.close();
		
	}
	
	public void writeFileToStream(String filename, int lines, OutputStream os) throws Exception {
		BufferedReader dr=new BufferedReader(new FileReader(filename));
		System.out.println("readerok");
		BufferedWriter dw=new BufferedWriter(new OutputStreamWriter(os));
		System.out.println("writerok");
		for (int i = 0; i < lines; i++) {
			
			String line = dr.readLine();
			System.out.println(line);
			dw.write(line);
			dw.newLine();
		}
		dr.close();
		dw.close();
	}
	
	public void sendFileToAddr(String filename, String addr) throws Exception {
		BufferedReader dr=new BufferedReader(new FileReader(filename));
		int lines = 0;
		String line = dr.readLine();
		while (!line.equals("")) {
			lines++;
		}
		dr.close();
		int slaveport = Configuration.slaveListenPort;
		Socket socketDN = new Socket(addr, slaveport);
		InputStream isDN = socketDN.getInputStream();
		ObjectInputStream oisDN = new ObjectInputStream(isDN);
		OutputStream osDN = socketDN.getOutputStream();
		ObjectOutputStream oosDN = new ObjectOutputStream(osDN);
		
		Message message = new Message();
		message.setType('s');
		message.setFileName(filename);
		message.setLines(lines);
		oosDN.writeObject(message);
		writeFileToStream(filename, lines, osDN);
		
		Message msg = (Message)oisDN.readObject();
		socketDN.close();
		
	}
}
