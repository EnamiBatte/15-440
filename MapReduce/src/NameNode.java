package dfs;

import java.io.*;
import java.util.*;

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
	
	
	
	public void decide(String filename, ObjectOutputStream oos) throws Exception {
		ArrayList<String> ret = new ArrayList<String>();
		for (int i = 0; i < replication; i++) {
			// Unfinished
			ret.add(slaveaddr.get(current));
			current += 1;
			if (current >= slaveaddr.size()) {
				current = 0;
			}
		}
		Message response = new Message();
		response.setAddrList(ret);
		oos.writeObject(response);
		oos.flush();
	}
	
	public ArrayList<String> findFile(String filename) {
		return filenametoslaveaddr.get(filename);
	}
	
	public void listAll() {
		if (filenametoslaveaddr.isEmpty()) {
			System.out.println("No files");
		} else {
			// Unfinished
		}
	}
}
