package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class Assignment1 extends Application {

	// variables that may need to be used in loops and out of loops

	Group root = new Group();

	double z = 0;
	double newStartPosX;
	double newStartPosY;
	double startDrawX;
	double startDrawY;
	double xLength = 600;
	double yLength = 600;
	double originX = xLength / 2;
	double originY = yLength / 2;
	int minFreq = -70;
	int maxFreq = 70;
	int xStretch = 5;
	ArrayList<double[]> CfXandY = new ArrayList<double[]>();

	public void drawFunction() { // Hard coded function to draw a parabola in the middle of screen.

		ArrayList<Line> functionLines = new ArrayList<Line>();

		double xCoord = 0;

		double newEndX = xStretch * (xCoord) + xLength / 2;
		double newEndY = (-Math.pow((xCoord), 2) + yLength / 2);

		do { // Draws right part of parabola arc first.
			Line line = new Line();
			line.setStartX(newEndX);
			line.setStartY(newEndY);
			xCoord += 1;
			newEndX = xStretch * (xCoord) + xLength / 2; // 5 at the front is used to transform X values so parabola
															// appear bigger
			newEndY = (-Math.pow((xCoord), 2) + yLength / 2); // Changes y values so it appears as it would in a graph.
			line.setEndX(newEndX);
			line.setEndY(newEndY);
			functionLines.add(line);
		} while (newEndY >= 0);

		xCoord = 0; // resets x and y values so the left side of parabola can be drawn.
		newEndY = 300;
		do {
			Line line = new Line();
			line.setStartX(newEndX);
			line.setStartY(newEndY);
			xCoord -= 1; // Draws from vertex to the left of the parabola.
			newEndX = xStretch * (xCoord) + xLength / 2;
			newEndY = (-Math.pow((xCoord), 2) + yLength / 2);
			line.setEndX(newEndX);
			line.setEndY(newEndY);
			functionLines.add(line);
		} while (newEndY >= 0);

		for (int i = 0; i < functionLines.size(); i++) { // adds lines made by function to root, showing it on window

			root.getChildren().add(functionLines.get(i));

		}

	}

	public void drawFunction(ArrayList<double[]> array) { // draws function for any arraylist of points.
		ArrayList<Line> functionLines = new ArrayList<Line>();
		double pointX = originX; // sets default start point in middle
		double pointY = originY;
		for (int i = 1; i < array.size(); i++) {
			Line line = new Line();
			line.setStartX(pointX);
			line.setStartY(pointY);
			// FunctionDrawer has it so the middle is 0,0. Assignment1's origin is
			// is 300,300. This means it needs to be staggered to fit the screen.
			pointX = originX + array.get(i)[0];
			pointY = originY + array.get(i)[1];
			line.setEndX(pointX);
			line.setEndY(pointY);
			functionLines.add(line);
		}

		for (int i = 1; i < functionLines.size(); i++) {
			root.getChildren().add(functionLines.get(i)); // This skips first line, which is the line from origin
															// to first drawn point
		}

	}

	public ArrayList<double[]> loadFunction(String file) { // Takes X and Y points from a txt file and puts it into
															// arrayList.

		ArrayList<double[]> coordFunc = new ArrayList<double[]>();

		try { // Allows from program to not crash if file cannot be found

			File file1 = new File(file);
			Scanner scanner = new Scanner(file1);
			while (scanner.hasNextLine()) { // while the text file has another line in it.
				double coord[] = new double[2];
				String line = scanner.nextLine();
				int commaNum = line.indexOf(','); // saves the index of comma in the string.
				coord[0] = Double.parseDouble(line.substring(0, commaNum)); // creates a substring from the number
				// before the comma changes it to a double.

				coord[1] = -1 * Double.parseDouble(line.substring(commaNum + 1)); // creates a substring from number
																					// after comma, changes it to a
																					// double
																					// -1 is a hard fix that works
				coordFunc.add(coord);
			}
			scanner.close();
		} catch (FileNotFoundException e) {

			System.out.println("File not found");
		}
		return coordFunc;
	}

	public ArrayList<double[]> loadFunction() { // Takes hard coded X and Y points from the function y = x^2 and puts
												// them in an arrayList.
		ArrayList<double[]> coordFunc = new ArrayList<double[]>();

		double xCoord = 0; // Starts at vertex of function
		double yCoord = 300;

		do {
			double[] coord = new double[2];
			coord[0] = xStretch * (xCoord) + originX; // adds xCoord
			yCoord = (-Math.pow((xCoord), 2) + originY); // calculates and adds corresponding yCoord
			coord[1] = yCoord;
			coordFunc.add(coord);
			xCoord += 0.5; // this is to get more points saved.
		} while (yCoord >= 0); // while the line is still on the screen

		xCoord = -1; // Set to a point that hasn't been graphed yet, 1 less of the vertex, getting
						// the points for
						// the left side of the function

		do {
			double[] coord = new double[2];
			coord[0] = xStretch * (xCoord) + originX;
			yCoord = (-Math.pow((xCoord), 2) + originY);
			coord[1] = yCoord;
			coordFunc.add(0, coord); // Each time the loop happens, the xCoord gets smaller. Adding each set of
										// points to
			// the beginning allows the final order of the arrayList to be from smallest
			// xCoord to biggest.

			xCoord -= 0.5; // This is to get more points saved

		} while (yCoord >= 0);
		return coordFunc;
	}

	public double[] getCfXY(ArrayList<double[]> a, int freq) { // This calculates CfX and CfY for a given frequency.

		double[] CfXY = new double[2];
		double CfX = 0;
		double CfY = 0;
		double delta_T = 1.0 / (a.size() - 1); // sets delta T to be the step size

		for (int i = 0; i < a.size(); i += 1) { // for each given point in function

			double tp = (1.0 / a.size()) * i; // time at point. This increases in relation
												// to where point is.

			double givenX = a.get(i)[0];
			double givenY = a.get(i)[1];

			// System.out.println(tp);// (givenX + " " + givenY);
			CfX += (givenX * Math.cos(2 * Math.PI * freq * tp) // CfX and CfY formulas

					+ givenY * Math.sin(2 * Math.PI * freq * tp)) * delta_T;

			CfY -= (givenX * Math.sin(2 * Math.PI * freq * tp)

					- givenY * Math.cos(2 * Math.PI * freq * tp)) * delta_T;

		}
		CfXY[0] = CfX;
		CfXY[1] = CfY;
		return CfXY; // returns an array with index 0 as CfX and index 1 as CfY
	}

	@Override

	public void start(Stage primaryStage) throws Exception {

		ArrayList<Line> tracerLine = new ArrayList<Line>(); // ArrayList which holds lines that represent the
															// approximated function.

		ArrayList<Line> armLine = new ArrayList<Line>(); // ArrayList which holds all the lines that are approximating
															// the function.

		// Goes through every wanted frequency and finds CfX and Y for that frequency
		for (int freq = minFreq; freq < maxFreq; freq++) {
			double[] CfXYfreq = new double[3];
			CfXYfreq[0] = getCfXY(loadFunction(), freq)[0]; // DETERMINES WHICH FUNCTION YOU WISH TO TRACE
			CfXYfreq[1] = getCfXY(loadFunction(), freq)[1]; // put file name to read file
			CfXYfreq[2] = freq;
			CfXandY.add(CfXYfreq); // saves CfX, CfY, and freq of every line
		}

		startDrawX = originX; // Set starting point for approximated function
		startDrawY = originY;

		AnimationTimer timer = new AnimationTimer() { // Animation timer. This lets stuff move on window.

			@Override

			public void handle(long now) { // Each tick of animation timer, lines move by 1 Z

				armLine.clear(); // clears armLine so old lines aren't redrawn.

				root.getChildren().clear(); // Clears drawn modules from previous iterations.

				newStartPosX = originY; // Sets the point to draw first line from
				newStartPosY = originX;

				for (int i = 0; i < CfXandY.size(); i++) { // creates one line for each frequency
															// from lowest to highest.
					Line line = new Line(); // Initializes line

					line.setStartX(newStartPosX); // Sets the starting position of line to the origin or the
													// ending position of previous line
					line.setStartY(newStartPosY);

					// Calculates endpoint of line
					newStartPosX += (CfXandY.get(i)[0] * Math.cos(2 * Math.PI * CfXandY.get(i)[2] * z)

							- CfXandY.get(i)[1] * Math.sin(2 * Math.PI * CfXandY.get(i)[2] * z));

					newStartPosY += (CfXandY.get(i)[0] * Math.sin(2 * Math.PI * CfXandY.get(i)[2] * z)

							+ CfXandY.get(i)[1] * Math.cos(2 * Math.PI * CfXandY.get(i)[2] * z));

					// Sets endpoint of the line
					line.setEndX(newStartPosX);
					line.setEndY(newStartPosY);

					armLine.add(line); // Adds the new line to the ArrayList.

				}

				for (int i = 0; i < armLine.size(); i++) { // Draws all the lines in the arm.

					root.getChildren().add(armLine.get(i));

				}

				double newTEndX = armLine.get(armLine.size() - 1).getEndX(); // Gets X coordinate and Y coordinate of
																				// the end of the last line in the arm.
				double newTEndY = armLine.get(armLine.size() - 1).getEndY();

				Line line = new Line(startDrawX, startDrawY, newTEndX, newTEndY); // creates a line with previous end
																					// point and new endpoint.

				startDrawX = newTEndX; // sets new starting point of next iteration of AnimationTimer loop to be where
										// previous line ended.
				startDrawY = newTEndY;
				tracerLine.add(line);

				for (int i = 1; i < tracerLine.size(); i++) { // draws approximated function
					root.getChildren().add(tracerLine.get(i));
				}

				z += 1.0 / loadFunction().size(); // goes up so lines actually spin

				drawFunction();
				drawFunction(loadFunction("function.txt"));

			}

		};
		for (int i = 0; i < loadFunction().size(); i++) {
			System.out.println(loadFunction().get(i)[0] + " " + loadFunction().get(i)[1]);
		}

		timer.start(); //

		Scene scene = new Scene(root, xLength, yLength); // Window setup

		primaryStage.setTitle("Assignment1");

		primaryStage.setScene(scene);

		primaryStage.show();

	}

	public static void main(String[] args) {

		launch(args);

	}

}
