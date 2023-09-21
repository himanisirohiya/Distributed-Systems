// Client2 class that
// sends data and receives also

import java.io.*;
import java.net.*;
import java.util.*;

class Client { 

	public static void main(String args[])
		throws Exception 
	{

		// Create client socket
		Socket s = new Socket("localhost", 888);
		// to send data to the server
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());

		// to read data coming from the server
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

		// to read data from the keyboard
		BufferedReader kb = new BufferedReader(new InputStreamReader(System.in));

		PrintWriter pr = new PrintWriter(s.getOutputStream(), true);
		OutputStream os = s.getOutputStream();

        Scanner scan = new Scanner(System.in);
		//send file contents
        // BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName));
		String str, str1, ch, ch1,ack, msg,opr, packedmsg="";

		byte[] buf = new byte[1024];
		// send to the server
		boolean cont = true;

        msg = br.readLine();
		while(true){
            System.out.println(msg.replaceAll("\t","\n"));
            ch = kb.readLine();
            
            // send the choice of the client to server
            //dos.writeBytes(ch + "\n");
                if(ch.equals("1")){
                    packedmsg = "calculate_pi";
                    dos.writeBytes(packedmsg + "\n");
                    ack = br.readLine();
                    System.out.println(ack);
                }
                if(ch.equals("2")){
                    // download the file from server
                    System.out.println("Enter 1st number: ");
                    String a = kb.readLine();
                    System.out.println("Enter 2nd number: ");
                    String b = kb.readLine();
                    packedmsg = "add "+a+" "+b;
                    dos.writeBytes(packedmsg + "\n");
                    ack = br.readLine();
                    System.out.println(ack);
                    //InputStream is = s.getInputStream();
                    //System.out.println("Result ="+br.readLine());
                    cont = true;
                }
                if(ch.equals("3")){
                    System.out.println("Enter the size of the array");
                    int arrsize = Integer.parseInt(kb.readLine());
                    String[] arr = new String[arrsize];
                    System.out.println("Enter array elements\n");
                    for(int i=0; i<arrsize; i++)
                        arr[i] = kb.readLine();
                    packedmsg = "sort "+Arrays.toString(arr);
                    dos.writeBytes(packedmsg + "\n");
                    ack = br.readLine();
                    System.out.println(ack);
                    //System.out.println("Result = "+br.readLine());
                    cont = true;
                }	
                if(ch.equals("4")){
                    // take the user input for each matrix
                    System.out.print("Enter size of matrix 1: ");
                    int r1 = scan.nextInt();
                    int c1 = scan.nextInt();
                    int mata [][]=new int [r1][c1];
                    System.out.println("Enter the elements of the matrix rowwise");
                    for(int j=0; j<r1; j++){
                        for(int k=0; k<c1; k++){
                            mata[j][k] = scan.nextInt(); 
                        }
                    }

                    System.out.print("Enter size of matrix 2: ");
                    int r2 = scan.nextInt();
                    int c2 = scan.nextInt();
                    int matb [][]=new int [r2][c2];
                    System.out.println("Enter the elements of the matrix rowwise");
                    for(int j=0; j<r2; j++){
                        for(int k=0; k<c2; k++){
                            matb[j][k] = scan.nextInt(); 
                        }
                    }
                    
                    System.out.print("Enter size of matrix 3: ");
                    int r3 = scan.nextInt();
                    int c3 = scan.nextInt();
                    int matc [][]=new int [r3][c3];
                    System.out.println("Enter the elements of the matrix rowwise");
                    for(int j=0; j<r3; j++){
                        for(int k=0; k<c3; k++){
                            matc[j][k] = scan.nextInt(); 
                        }
                    }
                    if(!(c1 == r2 && c2 == r3)){
                        System.out.println("Matrix multiplication not allowed because of inconsistent array sizes");
                    }
                    else{
                        //System.out.println(Arrays.deepToString(mata));
                        packedmsg = "mat_mul "+Integer.toString(r1)+" "+Integer.toString(c1)+" "+Arrays.deepToString(mata)+" "+Integer.toString(r2)+" "+Integer.toString(c2)+" "+Arrays.deepToString(matb)+" "+Integer.toString(r3)+" "+Integer.toString(c3)+" "+Arrays.deepToString(matc);
                        dos.writeBytes(packedmsg + "\n");
                        ack = br.readLine();
                        System.out.println(ack);
                    }
               
                    //System.out.println("Result:\n"+br.readLine());
                    cont = true;
                }

                if(ch.equals("5")){
                    System.out.println("Enter the operation for which you want to retrieve the result");
                    packedmsg = "retrieve";
                    dos.writeBytes(packedmsg + "\n");
                    System.out.println(br.readLine());
                    opr = kb.readLine();
                    dos.writeBytes(opr + "\n");
                    System.out.println("Result = "+br.readLine());
                }

                if(ch.equals("6"))
                    break;
            }

		// close connection.
		dos.close();
		br.close();
		kb.close();
		s.close();
	}
}
