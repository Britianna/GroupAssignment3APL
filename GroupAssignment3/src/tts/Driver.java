package tts;

import com.sun.speech.freetts.VoiceManager;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.sun.speech.freetts.Voice;

public class Driver {
	public static void main(String[] args) {
		try {
			Voice voice = initializeVoice();
			JFrame lizTalking = showTalkingAnimation();
			voice.speak("Welcome to the liz chatbox application");
			TimeUnit.SECONDS.sleep(1);
			lizTalking.dispose();

			loadGui(voice);
		} catch (MalformedURLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadGui(Voice voice) {
		JFrame f = new JFrame("Liz Chatbox");

		JButton b1 = new JButton("Read File");
		JButton b2 = new JButton("Read Text");
		JButton b4 = new JButton("Read PDF");
		JButton b5 = new JButton("Read Doc File");
		JButton b3 = new JButton("Exit");

		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				JOptionPane.showMessageDialog(null, "Warning, Liz currently only supports text and word files!!");

				JFileChooser fileBrowser = new JFileChooser(FileSystemView.getFileSystemView());

				int option = fileBrowser.showDialog(null, "Select");

				if (option == JFileChooser.APPROVE_OPTION) {
					try {
						File selectedFile = fileBrowser.getSelectedFile();

						String fileName = selectedFile.getName();

						int index = fileName.lastIndexOf('.');

						String extension = fileName.substring(index + 1);

						if (extension.contentEquals("txt")) {
							int sentenceLimit = Integer.parseInt(JOptionPane.showInputDialog("Enter sentence limit"));

							readTextFile(selectedFile, voice, sentenceLimit);

						} else if (extension.contentEquals("doc") || extension.contentEquals("docx")) {
							int sentenceLimit = Integer.parseInt(JOptionPane.showInputDialog("Enter sentence limit"));

							readWordFile(selectedFile, voice, sentenceLimit);
							
						} else if(extension.contentEquals("pdf")) {
							int sentenceLimit = Integer.parseInt(JOptionPane.showInputDialog("Enter sentence limit"));
							
							readPDFile(selectedFile, voice, sentenceLimit);
							
						} else {
							JOptionPane.showMessageDialog(null, "Invalid file type selected!!");
							System.out.println(extension);
						}
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Invalid limit, value must be an integer!!");

					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "!!UNEXPECTED RUNTIME ERROR ENCOUNTERED...shutting down!!");
						e.printStackTrace();
						System.exit(0);
					}
				} else {
					JOptionPane.showMessageDialog(null, "...Operation cancelled");
				}
			}
		});

		b2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTextField textField = new JTextField();
				JTextField limitField = new JTextField();

				Object[] form = { "Sentence Limit", limitField, "Text", textField };

				int option = JOptionPane.showConfirmDialog(null, form, "Enter Data", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					try {
						int sentenceLimit = Integer.parseInt(limitField.getText());
						String readMe = textField.getText();

						readText(readMe, voice, 0, sentenceLimit);

					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Invalid limit, value must be an integer!!");

					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "!!UNEXPECTED RUNTIME ERROR ENCOUNTERED...shutting down!!");
						e.printStackTrace();
						System.exit(0);
					}

				} else {
					JOptionPane.showMessageDialog(null, "Operation Canceled");
				}
			}
		});
		
		b4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JTextField textField2 = new JTextField();
				JTextField limitField2 = new JTextField();

				Object[] form = { "Sentence Limit", limitField2, "Text", textField2 };

				int option = JOptionPane.showConfirmDialog(null, form, "Enter Data", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					try {
						int sentenceLimit = Integer.parseInt(limitField2.getText());
						String readMe = textField2.getText();

						readText(readMe, voice, 0, sentenceLimit);

					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "Invalid limit, value must be an integer!!");

					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "!!UNEXPECTED RUNTIME ERROR ENCOUNTERED...shutting down!!");
						e.printStackTrace();
						System.exit(0);
					}

				} else {
					JOptionPane.showMessageDialog(null, "Operation Canceled");
				}
			}
		});


		b3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "...Goodbye (-_-)");
				System.exit(0);
			}
		});
		
		b5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser window = new JFileChooser();
				int returnValue = window.showOpenDialog(null);
				
				if(returnValue == JFileChooser.APPROVE_OPTION) {
					XWPFDocument document;
					try {
						document = new XWPFDocument(new FileInputStream(window.getSelectedFile()));
						XWPFWordExtractor extract = new XWPFWordExtractor(document);
						//System.out.print(extract.getText());
						//String doc=extract.getText();
						//voice.speak(removeDuplicateWords(doc));
						voice.speak(extract.getText());
						
						extract.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
			}
		});


		f.add(b1);
		f.add(b2);
		f.add(b4);
		f.add(b5);
		f.add(b3);
		

		f.setLayout(new GridLayout(5, 1));

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		f.setSize(400, 300);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	public static JFrame showTalkingAnimation() throws MalformedURLException {
		URL url = new URL("https://media.tenor.com/images/6eac85074dd9f02690a7d77279fc6054/tenor.gif");
		Icon icon = new ImageIcon(url);
		JLabel label = new JLabel(icon);

		// initialize frame and adds gif
		JFrame f = new JFrame("Shhhh...Liz is On");
		f.getContentPane().add(label);

		// f.setLayout(new GridLayout(1, 1));
		// set display parameters
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);

		return f;
	}

	public static String removeDuplicateWords(String sentence)
	{
		
		String regex = "\\b(\\w+)(?:\\W+\\1\\b)+";
		
		// possible regex for phrases: (\W|^)(.+)\s\2
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		
		Matcher m = pattern.matcher(sentence);
		
		while(m.find())
		{
			sentence = sentence.replaceAll(m.group(), m.group(1));
		}
		
		return sentence;
		
	}
	
	public static int readText(String currentLine, Voice voice, int sentenceCount, int sentenceLimit)
			throws MalformedURLException, InterruptedException {
		String[] splitLine;

		String leftOverLine = "";
		String currentSentence = "";

		if (currentLine.contains(".") || currentLine.contains("!") || currentLine.contains("?")) {
			// splits line into two arrays based on delimiter
			splitLine = currentLine.split("\\.|\\?|!", 2);
			currentSentence = splitLine[0];
			leftOverLine = splitLine[1];

			// reads the last sentence taken from file line
			currentSentence = currentSentence.replaceAll("[^a-zA-Z0-9 ]", "");

			voice.speak(removeDuplicateWords(currentSentence));

			// increments sentence count and exits function if sentence limit reached
			sentenceCount++;
			if (sentenceCount == sentenceLimit) {
				// lizTalking.dispose();
				return sentenceCount;
			}

			while (leftOverLine.contains(".") || leftOverLine.contains("!") || leftOverLine.contains("?")) {
				// splits line into two arrays based on delimiter
				splitLine = leftOverLine.split("\\.|\\?|!", 2);
				currentSentence = splitLine[0];
				leftOverLine = splitLine[1];

				// reads the last sentence taken from file line
				currentSentence = currentSentence.replaceAll("[^a-zA-Z0-9 ]", "");

				voice.speak(removeDuplicateWords(currentSentence));
				TimeUnit.SECONDS.sleep(2);

				// increments sentence count and exits function if sentence limit reached
				sentenceCount++;
				if (sentenceCount == sentenceLimit) {
					return sentenceCount;
				}
			}
		}

		return sentenceCount;
	}

	public static void readWordFile(File selectedFile3, Voice voice3, int sentenceLimit3) throws InterruptedException, FileNotFoundException, IOException{
		
		File file = selectedFile3;
		String currentLine3 = "";
		String leftOver3 = "";
		int sentenceCount3 = 0;
		
		// Creating Scanner instance to read from file
		   
		XWPFDocument document;
		
		    document = new XWPFDocument(new FileInputStream(file));
		    XWPFWordExtractor extract = new XWPFWordExtractor(document);
			
			String word = extract.getText();
			
		   Scanner fileScanner = new Scanner(word);
		 
		// Reading each line of file using Scanner class
		   
			while (fileScanner.hasNext()) {
				// read next line from file
				voice3.speak(removeDuplicateWords(word));
				currentLine3 = leftOver3 + " " + fileScanner.nextLine();
				System.out.println("CurrentLine : " + currentLine3);

				int index = 0;

				if (currentLine3.lastIndexOf('.') != -1) {
					index = currentLine3.lastIndexOf('.');
				} else if (currentLine3.lastIndexOf('!') != -1) {
					index = currentLine3.lastIndexOf('!');
				} else if (currentLine3.lastIndexOf('?') != -1) {
					index = currentLine3.lastIndexOf('?');
				}

				if (index != 0) {
					String sentences = currentLine3.substring(0, index + 1);

					leftOver3 = currentLine3.substring(index + 1);

					System.out.println("Sentences : " + sentences);
					System.out.println("LeftOver : " + leftOver3);

					sentenceCount3 = readText(sentences, voice3, sentenceCount3, sentenceLimit3);
					System.out.println(sentenceCount3);
				} else {
					leftOver3 = currentLine3;
					System.out.println("LeftOver: " + leftOver3);
				}

				if (sentenceCount3 == sentenceLimit3) {
					fileScanner.close();
					return;
				}
			}
			extract.close();
			fileScanner.close();
			return;
	}

	public static void readPDFile(File selectedFile2, Voice voice2, int sentenceLimit2)throws IOException, InterruptedException 
	{
		File file = selectedFile2;
		String currentLine2 = "";
		String leftOver2 = "";
		int sentenceCount2 = 0;
		
		// Creating Scanner instance to read from file
		   
		   PDDocument document;
		   document = PDDocument.load(file);
		   PDFTextStripper pdfStripper = new PDFTextStripper();
		   String text = pdfStripper.getText(document);
		   
		   
		   Scanner fileScanner = new Scanner(text);
		  // System.out.println(text);
		// Reading each line of file using Scanner class
		   
			while (fileScanner.hasNext()) {
				// read next line from file
				voice2.speak(removeDuplicateWords(text));
				currentLine2 = leftOver2 + " " + fileScanner.nextLine();
				System.out.println("CurrentLine : " + currentLine2);

				int index = 0;

				if (currentLine2.lastIndexOf('.') != -1) {
					index = currentLine2.lastIndexOf('.');
				} else if (currentLine2.lastIndexOf('!') != -1) {
					index = currentLine2.lastIndexOf('!');
				} else if (currentLine2.lastIndexOf('?') != -1) {
					index = currentLine2.lastIndexOf('?');
				}

				if (index != 0) {
					String sentences = currentLine2.substring(0, index + 1);

					leftOver2 = currentLine2.substring(index + 1);

					System.out.println("Sentences : " + sentences);
					System.out.println("LeftOver : " + leftOver2);

					sentenceCount2 = readText(sentences, voice2, sentenceCount2, sentenceLimit2);
					System.out.println(sentenceCount2);
				} else {
					leftOver2 = currentLine2;
					System.out.println("LeftOver 2 : " + leftOver2);
				}

				if (sentenceCount2 == sentenceLimit2) {
					fileScanner.close();
					return;
				}
			}
			document.close();
			fileScanner.close();
			return;

			
	}
	public static void readTextFile(File selectedFile, Voice voice, int sentenceLimit)
			throws FileNotFoundException, MalformedURLException, InterruptedException {
		// initializing values
		File file = selectedFile;
		String currentLine = "";
		String leftOver = "";
		int sentenceCount = 0;

		// Creating Scanner instance to read from file
		Scanner fileScanner = new Scanner(file);

		// Reading each line of file using Scanner class
		while (fileScanner.hasNext()) {
			// read next line from file
			currentLine = leftOver + " " + fileScanner.nextLine();
			System.out.println("CurrentLine : " + currentLine);

			int index = 0;

			if (currentLine.lastIndexOf('.') != -1) {
				index = currentLine.lastIndexOf('.');
			} else if (currentLine.lastIndexOf('!') != -1) {
				index = currentLine.lastIndexOf('!');
			} else if (currentLine.lastIndexOf('?') != -1) {
				index = currentLine.lastIndexOf('?');
			}

			if (index != 0) {
				String sentences = currentLine.substring(0, index + 1);

				leftOver = currentLine.substring(index + 1);

				System.out.println("Sentences : " + sentences);
				System.out.println("LeftOver : " + leftOver);

				sentenceCount = readText(sentences, voice, sentenceCount, sentenceLimit);
				System.out.println(sentenceCount);
			} else {
				leftOver = currentLine;
				System.out.println("LeftOver 2 : " + leftOver);
			}

			if (sentenceCount == sentenceLimit) {
				fileScanner.close();
				return;
			}
		}

		fileScanner.close();
		return;
	}

	public static Voice initializeVoice() {
		System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

		// initializing voice object
		Voice voice;
		voice = VoiceManager.getInstance().getVoice("kevin16");
		if (voice != null) {
			voice.allocate();
		}

		// setting voice properties
		voice.setRate(130);
		voice.setPitch(150);
		voice.setVolume(40);

		return voice;
	}

}
