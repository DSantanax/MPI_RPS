import mpi.MPI;

import java.util.Arrays;

/*
    Edit configuration to run -np [N] for number of processors where N is any number
    For program arguments enter the number of games ranging from 1-N
 */

public class MPITest {

    public static void main(String[] args) {
        //print out args
        //System.out.println(Arrays.toString(args);

        //args for program argument is at index 6
        int numberOfGames = Integer.parseInt(args[6]);

        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int numProc = MPI.COMM_WORLD.Size();

        System.out.println("Hello world from " + rank + " from " + numProc);

        //Each processor will have their own player
        Player player = new Player();
        //sendBuffer is for the player to send their roll
        char[] sendBuffer = new char[1];
        //each processor will receive the other rolls from the processors
        char[] recBuffer = new char[numProc];

        for (int i = 0; i < numberOfGames; i++) {
            //rank 0 displays the game number\
            if (rank == 0) {
                System.out.println("Game: " + (i + 1));
            }
            //each processor (player) will roll
            sendBuffer[0] = player.RPS_Roll();

            //barriers wait until all processors have called it
            //used for output
            MPI.COMM_WORLD.Barrier();
            System.out.println("Data in process (rank) " + rank + " before send:  " + Arrays.toString(sendBuffer));
            //send our roll and receive all the rolls
            MPI.COMM_WORLD.Allgather(sendBuffer, 0, 1, MPI.CHAR, recBuffer, 0, 1, MPI.CHAR);

            //Print out all the rolls for each processor to check if they received them
            MPI.COMM_WORLD.Barrier();
            System.out.println("Data in process (rank) " + rank + " received:  " + Arrays.toString(recBuffer));


            //logic for points
            checkRolls(player, recBuffer, rank);

            //clear buffer for next game
            recBuffer = new char[numProc];
            MPI.COMM_WORLD.Barrier();
        }
        MPI.Finalize();
    }

    private static void checkRolls(Player player, char[] recBuffer, int rank) {
        char playerRoll = recBuffer[rank];

        for (int i = 0; i < recBuffer.length; i++) {
            char oppRoll = recBuffer[i];
            if (rank != i && oppRoll != playerRoll) {
                if (playerRoll == 'R') {
                    if (oppRoll == 'S')
                        player.increasePoints();
//                    else
//                        player.decreasePoints();

                }
                if (playerRoll == 'P') {
                    if (oppRoll == 'R')
                        player.increasePoints();
//                    else
//                        player.decreasePoints();
                }
                if (playerRoll == 'S') {
                    if (oppRoll == 'P')
                        player.increasePoints();
//                    else
//                        player.decreasePoints();
                }
            }
        }
        MPI.COMM_WORLD.Barrier();
        System.out.println("Processor (Rank) " + rank + ": score: " + player.getPoints());
    }
}
