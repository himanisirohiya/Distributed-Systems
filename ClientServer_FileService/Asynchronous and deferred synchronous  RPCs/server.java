import java.io.*;
import java.net.*;
import java.util.*;

class Server
{   
    //unpack a simple string without arrays
    public static String[] unpack(String s){
        return s.split(" ");
    }

    //unpack strings with arrays
    public static String[] unpack_array(String s){
        s= s.replaceAll("\\[", "").replaceAll("\\]","").replaceAll(",","");
        return s.split(" ");
    }

    //function to calculate the value of pi()
    public static double calculate_pi(){
        double sum =0;
        //double value = 10000.0;
        for (double d = 1; d <= (2 * 10000 - 1); d += 2) {
            sum += 1 / d;
            d += 2;
            sum -= 1 / d; 
        }
        double pi = 4 * sum;
        return pi;
    }

    //function to add two numbers
    public static int add(int i, int j){
        return i+j;
    }

    //function to sort a 1d array
    public static String sorting(int[] a){
        for(int i=0; i<a.length-1; i++){
            for(int j=0; j<a.length-i-1; j++){
                if(a[j]>a[j+1]){
                    int temp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = temp;
                }
            }
        }
        return Arrays.toString(a);
    }

    //function to multiply three matrices
    public static int[][] matrix_multiply(int[][] matrixA, int[][] matrixB, int[][] matrixC, int r1, int c1, int r2, int c2, int r3, int c3){
        int C[][] = new int[r1][c2];
        int finalmat[][] = new int[r1][c3];
        for (int i = 0; i < r1; i++) {
            for (int j = 0; j < c2; j++) {
                for (int k = 0; k < r2; k++)
                    C[i][j] += matrixA[i][k] * matrixB[k][j];
            }
        }
        for (int i = 0; i < r1; i++) {
            for (int j = 0; j < c3; j++) {
                for (int k = 0; k < c2; k++)
                    finalmat[i][j] += C[i][k] * matrixC[k][j];
            }
        }
        return finalmat;
    }
    
    //function to get a proper 2d matrix after unoacking
    public static int[][] getMatrix(String[] mat, int row, int col, int start){
        int[][] matrix = new int[row][col];
        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                matrix[i][j] = Integer.parseInt(mat[start++]);
            }
        }
        return matrix;
    }

	public static void main(String args[]) throws Exception
	{
        // String p = System.getProperty("user.dir");
        // System.out.println("Path " + p);
		// Create server socket and accept client's connection
		ServerSocket ss = new ServerSocket(888);
        System.out.println("Waiting for client(s) to connect");
		Socket s = ss.accept();
        System.out.println("Connection established");
		// Create text output and input streams
		DataInputStream dis = new DataInputStream(s.getInputStream());
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        PrintStream ps = new PrintStream(s.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        Map<String, String> hm = new HashMap<String, String>();
        String result, str, choice;
        ps.println("Enter the operation to be performed:\t1.Compute_pi\t2.Add\t3.Sort\t4.Multiply matrix\t5.Retrieve Results\t6.Exit");
		while(true)
		{
		// 	// 02. Receive choice
		    //choice = dis.readUTF();
            //unpack the message from client and perform the operation specified in the comment
            str = br.readLine();
            String[] opr = unpack(str);
            String[] oprarr = unpack_array(str);
            
                if(opr[0].equals("calculate_pi")){
                    dos.writeBytes("Request to calculate_pi received\n");
                    result = Double.toString(calculate_pi());
                    //System.out.println(result);
                    //UUID pi = UUID.randomUUID();
                    // dos.writeUTF(result);
                    // dos.flush();
                    hm.put("pi",result);
                }
                if(opr[0].equals("add")){
                    dos.writeBytes("Request to add received\n");
                    result = Integer.toString(add(Integer.parseInt(opr[1]),Integer.parseInt(opr[2])));
                    // System.out.println(result);
                    // dos.writeUTF(result);
                    // dos.flush();
                    hm.put("add", result);
                } 

                if(oprarr[0].equals("sort")){
                    dos.writeUTF("Request to sort received\n");
                    int [] newa = new int [oprarr.length-1]; 
                    for(int i=1; i<oprarr.length; i++){
                        newa[i-1] =  Integer.parseInt(oprarr[i]);
                    }
                    System.out.println(Arrays.toString(newa));
                    String sorted = sorting(newa);
                    // System.out.println(sorted);
                    // dos.writeUTF(sorted);
                    // dos.flush();
                    hm.put("sort",sorted);
                }

                if(oprarr[0].equals("mat_mul")){
                    dos.writeUTF("Request to multiply matrices received\n");
                    int r1=Integer.parseInt(oprarr[1]);
                    int c1=Integer.parseInt(oprarr[2]);
                    int start =3;
                    int[][] mata = getMatrix(oprarr, r1, c1, start);

                    int idx = (r1*c1)+start;
                    int r2=Integer.parseInt(oprarr[idx]);
                    int c2=Integer.parseInt(oprarr[idx+1]);
                    start = idx+2;
                    int[][] matb = getMatrix(oprarr, r2, c2, start);

                    idx = (r2*c2)+start;
                    int r3=Integer.parseInt(oprarr[idx]);
                    int c3=Integer.parseInt(oprarr[idx+1]);
                    start = idx+2;
                    int[][] matc = getMatrix(oprarr, r3, c3, start);
                    String matmul = Arrays.deepToString(matrix_multiply(mata, matb, matc, r1, c1, r2, c2, r3, c3));
                    // dos.writeUTF(matmul);
                    // dos.flush();
                    hm.put("matmul",matmul);
                } 
 
                if(oprarr[0].equals("retrieve")){
                    Set keys = hm.keySet();
                    String joined = String.join(", ", keys);
                    dos.writeBytes(joined + "\n");
                    String operation = br.readLine();
                    String finalResult = hm.get(operation); 
                    dos.writeBytes(finalResult + "\n");
                }
                if(oprarr[0].equals("exit")){
                    break;
                }
        }
        s.close();
		ss.close();
    }
}
