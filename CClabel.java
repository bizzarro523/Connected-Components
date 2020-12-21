import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
public class CClabel {
	public int numRows, numCols, minVal, maxVal,
	newMin;
	int newMax = 1;
	public int newLabel = 0;
	public int minLabel;
	public int trueNumCC;

	int [][] zeroFramedAry;
	int [] nonZeroNeighborAry;
	int [] EQAry;

	public class Property{
		int label, minR, minC, maxR, maxC, numPixels;

		Property(){
			numPixels = 0;
		}
	}

	Property [] CCproperty;

	CClabel(Scanner inputFile){	
		numRows = inputFile.nextInt();
		numCols = inputFile.nextInt();
		minVal = inputFile.nextInt();
		maxVal = inputFile.nextInt();

		zeroFramedAry = new int [numRows+2][numCols+2];

		EQAry = new int [(numRows*numCols) / 4];
		for(int i = 0; i < EQAry.length; i++)
			EQAry[i] = i;	
	}

	public void minus1D(int [] ary) {
		for(int i = 0; i < ary.length;i++)
			ary[i] = -1;
	}

	public static int getMin(int[] inputArray){ 
		int minValue = inputArray[0]; 
		for(int i=1;i<inputArray.length;i++){ 
			if(inputArray[i] < minValue){ 
				minValue = inputArray[i]; 
			} 
		} 
		return minValue; 
	} 

	public static int getMax(int[] inputArray){ 
		int maxValue = inputArray[0]; 
		for(int i=1;i<inputArray.length;i++){ 
			if(inputArray[i] > maxValue){ 
				maxValue = inputArray[i]; 
			} 
		} 
		return maxValue; 
	} 


	public void loadImage(Scanner inFile) {
		int value;
		for(int i = 1; i <= numRows; i++) {
			for(int j = 1; j <= numCols; j++) {
				value = inFile.nextInt();
				zeroFramedAry[i][j] = value;
			}
		}			
	}

	public void imgReformat(int [][] ary, int newMax, BufferedWriter outFile) throws IOException{
		outFile.write(numRows + " " + numCols + " " + newMin + " " + newMax + "\n");
		String str = Integer.toString(newMax);
		int width = str.length();
		int r = 0;
		while (r <= numRows + 1)
		{
			int c = 0;
			while (c <= numCols + 1)
			{
				if(ary[r][c] > 0 )
					outFile.write(ary[r][c] + "");
				else 
					outFile.write(".");
				str = Integer.toString(ary[r][c]);
				int ww = str.length();

				while (ww <= width)
				{
					outFile.write(" ");
					ww++;
				}
				c++;
			}
			outFile.write("\n");
			r++;
		}
	}

	public void connect4Pass1() {
		for(int i = 1; i <= numRows; i++) {
			for(int j = 1; j <= numCols; j++) {
				if(zeroFramedAry[i][j] != 0){
					if( (zeroFramedAry[i-1][j] == 0) && (zeroFramedAry[i][j-1] == 0)) { //case 1
						newLabel++;
						zeroFramedAry[i][j] = newLabel;
					}
					else if( (zeroFramedAry[i-1][j] > 0 && zeroFramedAry[i][j-1] == 0)  || (zeroFramedAry[i-1][j] == 0 && zeroFramedAry[i][j-1] != 0)) { //case 2
						if(zeroFramedAry[i-1][j] >  0)
							zeroFramedAry[i][j] = zeroFramedAry[i-1][j];
						else
							zeroFramedAry[i][j] = zeroFramedAry[i][j-1];
					}
					else if (zeroFramedAry[i-1][j] == zeroFramedAry[i][j-1])
						zeroFramedAry[i][j] = zeroFramedAry[i][j-1];

					else{ 	//case 3
						minLabel = Math.min(zeroFramedAry[i-1][j], zeroFramedAry[i][j-1]);	
						EQAry[Math.max(zeroFramedAry[i-1][j], zeroFramedAry[i][j-1])] = minLabel;
						zeroFramedAry[i][j] = minLabel;
					}
				}
			}
		}
		for(int i = 1; i <= numRows; i++) {
			for(int j = 1; j <= numCols; j++) {
				if(zeroFramedAry[i][j] > 0)
					if(zeroFramedAry[i][j] != EQAry[zeroFramedAry[i][j]])
						zeroFramedAry[i][j] = EQAry[zeroFramedAry[i][j]];

			}
		}
	}

	public void connect4Pass2() {
		for(int i = numRows; i >= 1; i--) {
			for(int j = numCols; j >= 1; j--) {
				if(zeroFramedAry[i][j] > 0){
					if( (zeroFramedAry[i][j+1] == 0) && (zeroFramedAry[i+1][j] == 0)) //case 1
						zeroFramedAry[i][j] = zeroFramedAry[i][j];
					else if( (zeroFramedAry[i][j+1] == zeroFramedAry[i+1][j]) && (zeroFramedAry[i][j+1] == zeroFramedAry[i][j])) { //case 2 
						zeroFramedAry[i][j] = zeroFramedAry[i][j];
					}
					else {		//case 3
						int n = 0;
						int count = 0;
						if(zeroFramedAry[i][j+1] > 0) {
							count++;
						}
						if(zeroFramedAry[i+1][j] > 0) {
							count++;
						}
						if(zeroFramedAry[i][j] > 0) {
							count++;
						}

						nonZeroNeighborAry = new int [count];

						if(zeroFramedAry[i][j+1] > 0) {
							nonZeroNeighborAry[n] = zeroFramedAry[i][j+1];
							n++;
						}
						if(zeroFramedAry[i+1][j] > 0) {
							nonZeroNeighborAry[n] = zeroFramedAry[i+1][j];
							n++;
						}
						if(zeroFramedAry[i][j] > 0) 
							nonZeroNeighborAry[n] = zeroFramedAry[i][j];

						minLabel = getMin(nonZeroNeighborAry);
						if(zeroFramedAry[i][j] > minLabel) {
							EQAry[zeroFramedAry[i][j]] = minLabel;
							zeroFramedAry[i][j] = minLabel;
						}
					}
				}
			}
		}

		for(int i = 1; i <= numRows; i++) {
			for(int j = 1; j <= numCols; j++) {
				if(zeroFramedAry[i][j] > 0)
					if(zeroFramedAry[i][j] != EQAry[zeroFramedAry[i][j]])
						zeroFramedAry[i][j] = EQAry[zeroFramedAry[i][j]];

			}
		}

	}


	public void connect8Pass1() {
		for(int i = 1; i <= numRows; i++) {
			for(int j = 1; j <= numCols; j++) {
				if(zeroFramedAry[i][j] != 0){
					//Check Case 1
					if((zeroFramedAry[i-1][j-1] == 0) && (zeroFramedAry[i-1][j] == 0) 
							&& (zeroFramedAry[i-1][j+1] == 0) && (zeroFramedAry[i][j-1] == 0)) {
						newLabel++;
						zeroFramedAry[i][j] = newLabel;
					} //end Case 1 check
					else {
						// Load Non Zero Neighbor Array
						int n = 0;
						int count = 0;
						if(zeroFramedAry[i-1][j-1] > 0)
							count++;
						if(zeroFramedAry[i-1][j] > 0)
							count++;
						if(zeroFramedAry[i-1][j+1] > 0)
							count++;
						if(zeroFramedAry[i][j-1] > 0)
							count++;

						nonZeroNeighborAry = new int[count];

						if(zeroFramedAry[i-1][j-1] > 0) {
							nonZeroNeighborAry[n] = zeroFramedAry[i-1][j-1];
							n++;
						}
						if(zeroFramedAry[i-1][j] > 0) {
							nonZeroNeighborAry[n] = zeroFramedAry[i-1][j];
							n++;
						}
						if(zeroFramedAry[i-1][j+1] > 0) {
							nonZeroNeighborAry[n] = zeroFramedAry[i-1][j+1];
							n++;
						}
						if(zeroFramedAry[i][j-1] > 0) {
							nonZeroNeighborAry[n] = zeroFramedAry[i][j-1];

						}//end loading of nonZeroNeighborAry

						//Case 2 check
						boolean equal = true;
						for(int a = 0; a < nonZeroNeighborAry.length - 1; a++)
							for(int b = a+1; b < nonZeroNeighborAry.length; b++)
								if(nonZeroNeighborAry[a] != nonZeroNeighborAry[b]) {
									equal = false;
									//break;
								}
						if(equal) {
							zeroFramedAry[i][j] = nonZeroNeighborAry[0];
						}//end Case 2 check

						//Case 3
						else {
							minLabel = getMin(nonZeroNeighborAry);	
							EQAry[getMax(nonZeroNeighborAry)] = minLabel;
							zeroFramedAry[i][j] = minLabel;
						} //end Case 3 check
					}
				}
			}
		}
		for(int i = 1; i <= numRows; i++) {
			for(int j = 1; j <= numCols; j++) {
				if(zeroFramedAry[i][j] > 0)
					if(zeroFramedAry[i][j] != EQAry[zeroFramedAry[i][j]])
						zeroFramedAry[i][j] = EQAry[zeroFramedAry[i][j]];
			}
		}
	}

	public void connect8Pass2() {
		for(int i = numRows; i >= 1; i--) {
			for(int j = numCols; j >= 1; j--) {
				if(zeroFramedAry[i][j] > 0){
					//Check Case 1
					if(zeroFramedAry[i][j+1] == 0 && zeroFramedAry[i+1][j-1] == 0 
							&& zeroFramedAry[i+1][j] == 0 && zeroFramedAry[i+1][j+1] == 0)
						zeroFramedAry[i][j] = zeroFramedAry[i][j];
					//end Case 1 check
					else {
						// Load Non Zero Neighbor Array
						int n = 0;
						int count = 0;
						if(zeroFramedAry[i][j+1] > 0)
							count++;
						if(zeroFramedAry[i+1][j-1] > 0)
							count++;
						if(zeroFramedAry[i+1][j] > 0)
							count++;
						if(zeroFramedAry[i+1][j+1] > 0)
							count++;
						if(zeroFramedAry[i][j] > 0) 
							count++;

						nonZeroNeighborAry = new int[count];

						if(zeroFramedAry[i][j+1] > 0) {
							nonZeroNeighborAry[n] = zeroFramedAry[i][j+1];
							n++;
						}
						if(zeroFramedAry[i+1][j-1] > 0) {
							nonZeroNeighborAry[n] = zeroFramedAry[i+1][j-1];
							n++;
						}
						if(zeroFramedAry[i+1][j] > 0) {
							nonZeroNeighborAry[n] = zeroFramedAry[i+1][j];
							n++;
						}
						if(zeroFramedAry[i+1][j+1] > 0) {
							nonZeroNeighborAry[n] = zeroFramedAry[i+1][j+1];
							n++;
						}
						if(zeroFramedAry[i][j] > 0)
							nonZeroNeighborAry[n] = zeroFramedAry[i][j];
						//end loading of nonZeroNeighborAry

						//Case 2 check
						boolean equal = true;
						for(int a = 0; a < nonZeroNeighborAry.length - 1; a++)
							for(int b = a+1; b < nonZeroNeighborAry.length; b++)
								if(nonZeroNeighborAry[a] != nonZeroNeighborAry[b]) {
									equal = false;
									break;
								}
						if(equal) {
							//zeroFramedAry[i][j] = nonZeroNeighborAry[0];
							zeroFramedAry[i][j] = zeroFramedAry[i][j];
						}//end Case 2 check

						//Case 3
						minLabel = getMin(nonZeroNeighborAry);
						if(zeroFramedAry[i][j] > minLabel) {
							EQAry[zeroFramedAry[i][j]] = minLabel;
							zeroFramedAry[i][j] = minLabel;
						}//end Case 3
					}
				}
			}
		}
		for(int i = 1; i <= numRows; i++) {
			for(int j = 1; j <= numCols; j++) {
				if(zeroFramedAry[i][j] > 0)
					if(zeroFramedAry[i][j] != EQAry[zeroFramedAry[i][j]])
						zeroFramedAry[i][j] = EQAry[zeroFramedAry[i][j]];
			}
		}
	}

	public void printEQAry(int label, BufferedWriter outFile) throws IOException{
		for(int i = 0; i <= label; i++) {
			outFile.write(EQAry[i] + " ");
		}
	}

	public int manageEQAry(int []arr, int label) {
		int readLabel = 0;
		int index = 1;
		while(index <= label) {
			if (index != EQAry[index])
				EQAry[index] = EQAry[EQAry[index]];
			else {
				readLabel++;
				EQAry[index] = readLabel;	
			}
			index++;
		}

		int i;
		int max = arr[0]; 

		for (i = 1; i <= label; i++) 
			if (arr[i] > max) 
				max = arr[i]; 

		return max;  
	}

	public void connectPass3(int maxLabel) {
		CCproperty = new Property[maxLabel + 1];
		for(int i = 0; i < CCproperty.length; i++) {
			CCproperty[i] = new Property();
			CCproperty[i].minR = 99;
			CCproperty[i].minC = 99;
			CCproperty[i].maxR = 0;
			CCproperty[i].maxC = 0;
		}

		for(int i = 1; i <= numRows; i++) {
			for(int j = 1; j <= numCols; j++) {
				if(zeroFramedAry[i][j] > 0) {
					zeroFramedAry[i][j] = EQAry[zeroFramedAry[i][j]];
				}
			}
		}
		for(int i = 1; i <= numRows; i++) {
			for(int j = 1; j <= numCols; j++) {
				if(zeroFramedAry[i][j] > 0) {
					CCproperty[zeroFramedAry[i][j]].label = zeroFramedAry[i][j];
					CCproperty[zeroFramedAry[i][j]].numPixels++;
					int minRowVal = i;
					if(minRowVal < CCproperty[zeroFramedAry[i][j]].minR)
						CCproperty[zeroFramedAry[i][j]].minR = minRowVal ; 
					int minColVal = j;
					if(minColVal < CCproperty[zeroFramedAry[i][j]].minC)
						CCproperty[zeroFramedAry[i][j]].minC = minColVal; 

					int maxRowVal = i;
					if(maxRowVal > CCproperty[zeroFramedAry[i][j]].maxR)
						CCproperty[zeroFramedAry[i][j]].maxR = maxRowVal; 
					int maxColVal = j;
					if(maxColVal > CCproperty[zeroFramedAry[i][j]].maxC)
						CCproperty[zeroFramedAry[i][j]].maxC = maxColVal; 
				}
			}
		}
	}

	public void printImg(BufferedWriter outFile, int max) throws IOException{
		outFile.write(numRows + " " + numCols + " " + newMin + " " + max + "\n");
		String str = Integer.toString(max);
		int width = str.length();
		int r = 1;
		while (r <= numRows)
		{
			int c = 1;
			while (c <= numCols)
			{
				if(zeroFramedAry[r][c] > 0)
					outFile.write(zeroFramedAry[r][c] + "");
				else
					outFile.write("."); 
				str = Integer.toString(zeroFramedAry[r][c]);
				int ww = str.length();

				while (ww <= width)
				{
					outFile.write(" ");
					ww++;
				}
				c++;
			}
			outFile.write("\n");
			r++;
		}
	}

	public void printCCproperty(BufferedWriter outFile, int max) throws IOException{
		outFile.write(numRows + " " + numCols + " " + newMin + " " + max + "\n");
		outFile.write(max + "\n\n");
		for(int i = 1; i < CCproperty.length; i++) {
			outFile.write(CCproperty[i].label + "\n");			
			outFile.write(CCproperty[i].numPixels + "\n");
			outFile.write(CCproperty[i].minR + " " + CCproperty[i].minC + "\n");
			outFile.write(CCproperty[i].maxR + " " + CCproperty[i].maxC + "\n\n");
		}
	}

	public void drawBoxes(int [][]ary, Property []propAry) {
		int index  = 1;
		while(index < propAry.length) {
			int minRow = propAry[index].minR;
			int minCol = propAry[index].minC;
			int maxRow = propAry[index].maxR;
			int maxCol = propAry[index].maxC;
			int label = propAry[index].label;

			for(int j = minCol; j <= maxCol; j++)
				zeroFramedAry[minRow][j] = label;
			for(int j = minCol; j <= maxCol; j++)
				zeroFramedAry[maxRow][j] = label;
			for(int j = minRow; j <= maxRow; j++)
				zeroFramedAry[j][minCol] = label;
			for(int j = minRow; j <= maxRow; j++)
				zeroFramedAry[j][maxCol] = label;
			index++;
		}
	}
}

