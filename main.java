import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

public class main {
	public static void main (String[] args) throws IOException {
		Scanner inFile = new Scanner(new FileReader(args[0]));
		BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in));


		BufferedWriter RFprettyPrintFile = new BufferedWriter(new FileWriter(args[1])); 
		BufferedWriter labelFile = new BufferedWriter(new FileWriter(args[2])); 
		BufferedWriter propertyFile = new BufferedWriter(new FileWriter(args[3])); 

		CClabel cclabel = new CClabel(inFile);
		cclabel.loadImage(inFile);
		RFprettyPrintFile.write("Original Binary Input with Zero Frame:\n");
		cclabel.imgReformat(cclabel.zeroFramedAry, cclabel.newMax, RFprettyPrintFile);

		System.out.println("Which connectness would you like?");
		int whichConnectness = Integer.parseInt(reader.readLine()); //asks reader for connectness

		if(whichConnectness == 4) {
			cclabel.connect4Pass1();
			RFprettyPrintFile.write("\nPass 1 for 4-Connectness:\n");
			cclabel.imgReformat(cclabel.zeroFramedAry, cclabel.newLabel, RFprettyPrintFile);
			RFprettyPrintFile.write("\nEQAry after Pass 1 for 4-Connectness:\n");
			cclabel.printEQAry(cclabel.newLabel, RFprettyPrintFile);
			cclabel.connect4Pass2();
			RFprettyPrintFile.write("\n\nPass 2 for 4-Connectness:\n");
			cclabel.imgReformat(cclabel.zeroFramedAry, cclabel.newLabel, RFprettyPrintFile);
			RFprettyPrintFile.write("\nEQAry after Pass 1 for 4-Connectness:\n");
			cclabel.printEQAry(cclabel.newLabel, RFprettyPrintFile);
		}
		if(whichConnectness == 8) {
			cclabel.connect8Pass1();
			RFprettyPrintFile.write("\nPass 1 for 8-Connectness:\n");
			cclabel.imgReformat(cclabel.zeroFramedAry, cclabel.newLabel, RFprettyPrintFile);
			RFprettyPrintFile.write("\nEQAry after Pass 1 for 8-Connectness:\n");
			cclabel.printEQAry(cclabel.newLabel, RFprettyPrintFile);
			cclabel.connect8Pass2();
			RFprettyPrintFile.write("\n\nPass 2 for 8-Connectness:\n");
			cclabel.imgReformat(cclabel.zeroFramedAry, cclabel.newLabel, RFprettyPrintFile);
			RFprettyPrintFile.write("\nEQAry after Pass 1 for 8-Connectness:\n");
			cclabel.printEQAry(cclabel.newLabel, RFprettyPrintFile);
		}
		
		cclabel.trueNumCC = cclabel.manageEQAry(cclabel.EQAry, cclabel.newLabel);
		RFprettyPrintFile.write("\nEQAry after manageEQAry()\n");
		cclabel.printEQAry(cclabel.newLabel, RFprettyPrintFile);

		
		cclabel.connectPass3(cclabel.trueNumCC);
		RFprettyPrintFile.write("\n\nPass 3:\n");
		cclabel.imgReformat(cclabel.zeroFramedAry, cclabel.trueNumCC, RFprettyPrintFile);
		cclabel.printImg(labelFile, cclabel.trueNumCC);

		cclabel.printCCproperty(propertyFile, cclabel.trueNumCC);
		
		cclabel.drawBoxes(cclabel.zeroFramedAry, cclabel.CCproperty);
		RFprettyPrintFile.write("\n\nDraw Boxes:\n");
		cclabel.imgReformat(cclabel.zeroFramedAry, cclabel.trueNumCC , RFprettyPrintFile);
		
		inFile.close();
		RFprettyPrintFile.close();
		labelFile.close();
		propertyFile.close();	
	}
}