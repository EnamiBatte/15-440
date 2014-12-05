import mpi.*;
class Hello {
    static public void main(String[] args) {
        MPI.Init(args) ;
        int myrank = MPI.COMM_WORLD.Rank() ;
        if(myrank == 0) {
            char[] message = "Hello, there".toCharArray() ;
            MPI.COMM_WORLD.Send(message, 0, message.length, MPI.CHAR, 1, 99) ;
        }
        else {
            char[] message = new char [20] ;
            MPI.COMM_WORLD.Recv(message, 0, 20, MPI.CHAR, 0, 99) ;
            System.out.println("received:" + new String(message) + ":") ;
        }
        MPI.Finalize() ;
    }
}
