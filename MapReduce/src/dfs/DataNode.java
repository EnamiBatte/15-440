package dfs;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

import util.*;

public class DataNode {
	
	Map<String, HashSet<String>> fileMap;
	
	
	public DataNode() {
		fileMap = new HashMap<String, HashSet<String>>(); 
	}
	

	public String inputFileName(String partitionFileName) {
		if (partitionFileName.startsWith("m")) {
			String[] split = partitionFileName.split("_", 2);
			return split[1];
		} else {
			String[] split = partitionFileName.split("_", 2);
			return split[1].substring(0, split[1].lastIndexOf("_"));
		}
	}
	
	public void deleteFile(String filename) {
		File f = new File(filename);
		if (f.exists()) {
			f.delete();
		}
	}
	
	public void deleteJob(String filename) {
		if (fileMap.containsKey(filename)) {
			HashSet<String> val = fileMap.get(filename);
			for (String v : val) {
				deleteFile(v);
			}
			fileMap.remove(filename);
		}
	}
	public void deleteAll() {
		Iterator<Entry<String, HashSet<String>>> it = fileMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, HashSet<String>> entry = (Map.Entry<String, HashSet<String>>)it.next();
			HashSet<String> val = entry.getValue();
			for (String v : val) {
				deleteFile(v);
			}
		}
		fileMap.clear();
	}
	
	public int hash(String key, int num) {
		//System.out.print(key+" " + key.hashCode() % num + " ");
		return key.hashCode() % num;
	}
	
	public ArrayList<String> createLocalInputPartition(String filename, int numberOfLines) throws Exception {
		System.out.println("Creating local partition");
		Set<String> fileSet = new HashSet<String>();
		if (fileMap.containsKey("filename")) {
			fileSet = fileMap.get(filename);
		} 
		BufferedReader dr=new BufferedReader(new FileReader(filename));
		int lines = 0;
		String line = dr.readLine();
		while (!line.equals("")) {
			lines++;
			line = dr.readLine();
			//System.out.println(line);
		}
		dr.close();
		System.out.println("The file had the following number of lines: "+String.valueOf(lines));
		int numberOfFiles = (lines - 1) / numberOfLines + 1;
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<BufferedWriter> dw = new ArrayList<BufferedWriter> ();
		for (int i = 0; i < numberOfFiles; i++) {
			String name = "m" + i + "_" + filename;
			fileSet.add(name);
			files.add(name);
			File myFile = new File(name);
			myFile.createNewFile();
			dw.add(new BufferedWriter(new FileWriter(name)));
		}
		fileMap.put(filename, (HashSet<String>)fileSet);
		System.out.println("Files are successfully created");
		dr=new BufferedReader(new FileReader(filename));
		lines = 0;
		int i = 0;
		line = dr.readLine();
		//System.out.println(line);
		while (!line.equals("")) {
			lines++;
			dw.get(i).write(line);
			dw.get(i).newLine();
			if (lines == numberOfLines) {
				i++;
				lines = 0;
			}
			line = dr.readLine();
			//.out.println(line);
		}
		while ((lines < numberOfLines) && (lines!=0)) {
			dw.get(i).newLine();
			lines++;
		}
		dr.close();
		for (BufferedWriter d : dw) {
			d.close();
		}
		System.out.println("Files are successfully written");
		ArrayList<String> ret = new ArrayList<String>();
		for (String name : files) {
			ret.add(numberOfLines + "_" + name);
		}
		return ret;
	}
	public ArrayList<String> createLocalOutputPartition(String filename, int numberOfReducers) throws Exception {
		Set<String> fileSet = new HashSet<String>();
		if (fileMap.containsKey("filename")) {
			fileSet = fileMap.get(filename);
		} 
		BufferedReader dr=new BufferedReader(new FileReader(filename));
		int[] lines = new int[numberOfReducers];
		String line = dr.readLine();
		while (!line.equals("")) {
			int j = line.indexOf("|");
			String key = line.substring(0, j);
			int i = hash(key, numberOfReducers);
			lines[i]++;
			line = dr.readLine();
		}
		dr.close();
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<BufferedWriter> dw = new ArrayList<BufferedWriter> ();
		for (int i = 0; i < numberOfReducers; i++) {
			String name = "r" + filename+ "_" + i;
			fileSet.add(name);
			files.add(name);
			File myFile = new File(name);
			myFile.createNewFile();
			dw.add(new BufferedWriter(new FileWriter(name)));
		}
		fileMap.put(filename, (HashSet<String>)fileSet);
		dr=new BufferedReader(new FileReader(filename));
		line = dr.readLine();
		while (!line.equals("")) {
			int j = line.indexOf("|");
			String key = line.substring(0, j);
			int i = hash(key, numberOfReducers);
			dw.get(i).write(line);
			dw.get(i).newLine();
			line = dr.readLine();
		}
		for (BufferedWriter d : dw) {
			d.close();
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
		String ad = new String();
		for (int i = 0; i < Configuration.masterListenPorts.length; i++) {
			if (Master_port == Configuration.masterListenPorts[i]) {
				ad = Configuration.slaveAddress[i];
			}
		}
		
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
		System.out.println(localPartition.size() + "");
		for (int i = 0; i < localPartition.size(); i++) {
			
			Socket socket = new Socket(masterIP, Master_port);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("socket to master");
			String a = localPartition.get(i);
			String arr[] = a.split("_", 2);
			int lines = Integer.parseInt(arr[0]);
			String partitionFilename = arr[1];
		
			Message message = new Message();
			message.setType('s');
			message.setFileName(partitionFilename);
			oos.writeObject(message);
			System.out.println("message sent");
			Message response = (Message)ois.readObject();
			ArrayList<String> addrList = response.getAddrList();
			System.out.println("list received");
			for(String addr : addrList) {
				if (addr.equals(ad)) {
					continue;
				}
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
				
				Thread.sleep(100);
				socketDN.close();
			}
			Thread.sleep(100);
			socket.close();
		}
		
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
		String origin = inputFileName(filename);
		HashSet<String> val = new HashSet<String>();
		if (fileMap.containsKey(origin)) {
			val = fileMap.get(origin);
		}
		val.add(filename);
		fileMap.put(origin, val);
	}
	
	public void writeFileToStream(String filename, int lines, OutputStream os) throws Exception {
		BufferedReader dr=new BufferedReader(new FileReader(filename));
		System.out.println("readerok");
		BufferedWriter dw=new BufferedWriter(new OutputStreamWriter(os));
		System.out.println("writerok");
		for (int i = 0; i < lines; i++) {
			
			String line = dr.readLine();
			//System.out.println(line);
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
