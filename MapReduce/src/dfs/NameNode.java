package dfs;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import util.*;

public class NameNode {
	ArrayList<String> slaveaddr;
	HashMap<String, ArrayList<String>> filenametoslaveaddr;
	private int replication;
	private int current;
	
	public NameNode(ArrayList<String> slaveaddr, int replication) {
		this.slaveaddr = slaveaddr;
		this.replication = replication;
		this.filenametoslaveaddr = new HashMap<String, ArrayList<String>>();
		current = 0;
	}
	
	public Message decide(String filename) throws Exception {
		ArrayList<String> ret = new ArrayList<String>();
		if (filename.startsWith("r")) {
			String name = filename.substring(0, filename.lastIndexOf("_"));
			for (String key : filenametoslaveaddr.keySet()) {
				if (key.contains(name)) {
					ret = filenametoslaveaddr.get(key);
					filenametoslaveaddr.put(filename, ret);
					Message response = new Message();
					response.setAddrList(ret);;
					return response;
				}
				
			}
		}
		for (int i = 0; i < replication; i++) {
			ret.add(slaveaddr.get(current));
			current += 1;
			if (current >= slaveaddr.size()) {
				current = 0;
			}
		}
		filenametoslaveaddr.put(filename, ret);
		Message response = new Message();
		response.setAddrList(ret);
		return response;
	}
	
	
	public ArrayList<String> findFile(String filename) {
		return filenametoslaveaddr.get(filename);
	}
	
	public void listAll() {
		if (filenametoslaveaddr.isEmpty()) {
			System.out.println("No files");
		} else {
			Iterator<Entry<String, ArrayList<String>>> it = filenametoslaveaddr.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, ArrayList<String>> entry = (Map.Entry<String, ArrayList<String>>)it.next();  
				String key = entry.getKey(); 
				ArrayList<String> val = entry.getValue();
				System.out.println(key + ":");
				for (String v : val) {
					System.out.print(v + ";");
				}
				System.out.print("\n");
			}
		}
	}
}
