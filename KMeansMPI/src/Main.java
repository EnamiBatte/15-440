import mpi.*;

public class Main {

	public static void main(String[] args) throws Exception {
		MPI.Init(args);
		
		int rank;
		rank = MPI.COMM_WORLD.Rank();
		if(rank == 0)
		{
			System.out.println("Master");
			Master.run(args);
		}
		else{
			System.out.println("Slave");
			System.out.println(rank);
			Slave.run();
		}
		
		MPI.Finalize();
	}

}
