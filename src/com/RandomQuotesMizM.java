package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class RandomQuotesMizM
{
	/**
	 * Creates a input style message box dialog
	 * 
	 * @see createMessageBox if no user input is desired
	 * @see createOptionMessageBox if the user input is a "yes no" type or similar
	 * 
	 * @param text
	 *            a message
	 * @param caption
	 *            a caption; title
	 * @param selectionValues
	 *            values to be selected can be null
	 * @param defaultSelection
	 *            default value to be selected can be null
	 * @param type
	 *            type of message box (info, warning, question, etc...)
	 * @param <T>
	 *            template for selections
	 * 
	 * @return the JOptionPane.showInputDialog return value casted to string
	 */
	public static <T> String createInputMessageBox(String text, String caption, T[] selectionValues, T defaultSelection, int type)
	{
		return (String) JOptionPane.showInputDialog(null, text, caption, type, null, selectionValues, defaultSelection);
	}

	/**
	 * Creates a message box
	 * 
	 * @see createInputMessageBox if you want to obtain user input from the user
	 * @see createOptionMessageBox if the user input is a "yes no" type or similar
	 * 
	 * @param text
	 *            a message
	 * @param caption
	 *            a caption (the title)
	 * @param type
	 *            type of message box (info, warning, question, etc...)
	 */
	public static void createMessageBox(String text, String caption, int type)
	{
		JOptionPane.showMessageDialog(null, text, caption, type);
	}

	/**
	 * Creates an option message box
	 * 
	 * @see createInputMessageBox if you want to obtain user input from the user
	 * @see createMessageBox if no user input is desired
	 * 
	 * @param text
	 *            a message
	 * @param caption
	 *            a caption (the title)
	 * @param type
	 *            type of message box (info, warning, question, etc...)
	 * 
	 * @return the users choice
	 */
	public static int createOptionMessageBox(String text, String caption, int type)
	{
		return JOptionPane.showConfirmDialog(null, text, caption, type);
	}

	/**
	 * 
	 * @param file
	 *            a valid file object to read
	 * 
	 * @return the contents of the file
	 */
	public static ArrayList<String> readFile(File file)
	{
		ArrayList<String> quotes = null;
		
		// if the file does not exist don't go any further!
		if (!file.exists())
		{
			logError("File %s does not exist!", file.getAbsolutePath());
			return null;
		}
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			/*
			 * Supplier<R>, BiConsumer<R, ? super T>, BiConsumer<R,R> - are all function interfaces 
			 * (java's function pointers) We've supplied three lambdas which is sufficient enough
			 * 
			 * collect(Supplier<R> supplier, BiConsumer<R,? super T> accumulator, BiConsumer<R,R> combiner)
			 * Performs a mutable reduction operation on the elements of this stream.
			 */
			quotes = reader.lines().collect(() -> new ArrayList<String>(), (collection, element) -> collection.add(element.toString()), (dst, src) -> dst.addAll(src));
			reader.close();
		}
		catch (IOException e)
		{
			return null;
		}
		return quotes;
	}

	/**
	 * Logs an error message to the error output stream
	 * 
	 * @param errorMsg
	 *            a the message to display
	 * 
	 * @param args
	 *            any arguments for the error message
	 */
	private static void logError(String errorMsg, Object... args)
	{
		System.err.println(String.format(errorMsg, args));
	}

	
	/**
	 * Returns a random integer within the minimum and maximum 
	 * constraints
	 * 
	 * @param min
	 * 			the minimum constraint
	 * 
	 * @param max
	 * 			the maximum constraint
	 * 
	 * @return a random integer that conforms to the minimum and
	 * maximum constraints
	 */
	private static int randomInteger(int min, int max)
	{
		return (int)(Math.random() * (max - min + 1) + min);
	}
	
	/**
	 * Returns a formated string like a paragraph
	 * 
	 * @param msg
	 * 			a message
	 * @param nCharsWidth
	 * 			 how width the paragraph should be in characters
	 * 
	 * @return the input message formatted to a paragraph
	 */
	private static String makeParagraph(String msg, int nCharsWidth)
	{
		String paragraph = new String();
		
		if (msg.length() <= nCharsWidth)
			return msg;
		
		boolean bFirst = true;
		int iTruncate = nCharsWidth;
			
		for (; iTruncate < msg.length(); iTruncate += nCharsWidth)
		{
			for (int iNextSpace = iTruncate; iNextSpace > 0; --iNextSpace)
			{
				if (msg.charAt(iNextSpace) == 0x20)
				{
					if (bFirst)
					{
						paragraph = msg.substring(0, iNextSpace);
						bFirst = false;
					}
					
					paragraph += "\n";
					int iEnd = 0;
					
					if (iNextSpace + nCharsWidth >= msg.length())
					{
						iEnd = msg.length();
					}
					else 
					{
						for (int iSpace = iNextSpace + nCharsWidth; iSpace > 0; --iSpace)
						{
							 if (msg.charAt(iSpace) == 0x20)
							 {
								 iEnd = iSpace;
								 break;
							 }
						}
					}
					
					paragraph = paragraph.concat(msg.substring(iNextSpace, iEnd));
					iTruncate = iEnd - nCharsWidth;
					break;
				}
			}
		}
		return paragraph;
	}
	
	/**
	 * Main Function (Entry Point)
	 * 
	 * @param args
	 * 			command line arguments if any
	 */
	public static void main(String[] args)
	{	
		File quoteFile = new File(createInputMessageBox("What file would you like to query for quotes?", "Random Quotes of The Day", null, null, JOptionPane.INFORMATION_MESSAGE));
		ArrayList<String> quotes = readFile(quoteFile);
		int result = 0;

		if (quotes != null)
		{
			do
				result = createOptionMessageBox(makeParagraph(quotes.get(randomInteger(0, quotes.size() - 1)), 50) + "\n\nWould you like another quote?", "Random Quotes of The Day", JOptionPane.YES_NO_OPTION);
			while (result == JOptionPane.YES_OPTION);
		}
	}
}
