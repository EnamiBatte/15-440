package dfs;

import java.io.*;
import java.net.*;
import java.util.*;

public class DataNode {
	
	public DataNode() {	
	}
	public ArrayList<String> createLocalInputPartition(String filename, int numberOfLines) {
		return new ArrayList<String>();
	}
	public ArrayList<String> createLocalOutputPartition(String filename, int numberOfReducers) {
		return new ArrayList<String>();
	}
	public void addFileToDFS(String filename, boolean flag) throws Exception{
		//flag: true for map input, false for map output
		String masterIP = Configuration.Master_Address;
		int masterPort = Configuration.Master_port;
		Socket socket = new Socket(masterIP, masterPort);
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		ObjectOutputStream oos = new ObjectOutputStream(os);
		
		ArrayList<String> localPartition = new ArrayList<String>();
		if (flag) {
			int numberOfLines = Configuration.numberOfLines;
			localPartition = createLocalInputPartition(filename, numberOfLines);
		} else {
			int numberOfReducers = Configuration.numberOfReducers;
			localPartition = createLocalInputPartition(filename, numberOfReducers);
		}
		for (int i = 0; i < localPartition.size(); i++) {
			String a = localPartition.get(i);
			String arr[] = a.split("_", 1);
			int lines = Integer.parseInt(arr[0]);
			String partitionFilename = arr[1];
			InetAddress inet = InetAddress.getLocalHost();
			String localAddr = inet.getHostAddress();
			Message message = new Message();
			message.setType('s');
			message.setFileName(partitionFilename);
			message.setAddr(localAddr);
			message.setPartition(i);
			oos.writeObject(message);
			
			Message response = (Message)ois.readObject();
			ArrayList<String> addrList = response.getAddrList();
			
			for(String addr : addrList) {
				int slaveport = Configuration.slaveport;
				Socket socketDN = new Socket(addr, slaveport);
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
			
		}
		socket.close();
		
		

		
	}
	
	public void receiveFileFromStream(String filename, InputStream is, int lines) throws Exception {
		BufferedReader dr=new BufferedReader(new InputStreamReader(is));
		FileOutputStream fos = new FileOutputStream(filename);
		BufferedWriter dw=new BufferedWriter(new OutputStreamWriter(fos));
		for (int i = 0; i < lines; i++) {
			dw.write(dr.readLine());
			dw.newLine();
		} 
		dr.close();
		dw.close();
	}
	
	public void writeFileToStream(String filename, int lines, OutputStream os) throws Exception {
		BufferedReader dr=new BufferedReader(new FileReader(filename));
		BufferedWriter dw=new BufferedWriter(new OutputStreamWriter(os));
		for (int i = 0; i < lines; i++) {
			dw.write(dr.readLine());
			dw.newLine();
		}
		dr.close();
		dw.close();
	}
	

	
	
	
}
