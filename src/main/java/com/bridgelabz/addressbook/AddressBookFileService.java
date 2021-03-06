package com.bridgelabz.addressbook;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.bridgelabz.addressbook.CustomException.ExceptionsType;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;


public class AddressBookFileService 
{
	
	private static final String FILE_PATH = "D:\\CSV_JSON_Day28\\src\\main\\java\\com\\bridgelabz";
	public static final String TEXT_FILE="/addressBook.txt";
	public static final String CSV_FILE="/addressBook.csv";
	
	HashMap<String, LinkedList<Contacts>> addressBooks = AddressbookConsoleService.addressBooks;
		//method to store all contacts to file
		public void writingToTextFile() 
		{
			//checking file is already there
			checkFile();
			
			StringBuffer addressBookBuffer = new StringBuffer();
			addressBooks.entrySet().stream()
			.map(books->books.getKey())
			.map(bookNames->{
				addressBookBuffer.append(bookNames+"\n");
				return addressBooks.get(bookNames); 
			})
			.forEach(contactInBook->addressBookBuffer.append(contactInBook+"\n"));		
			
			//Writing data into file

			try 
			{
				if (!TEXT_FILE.split("\\.")[1].equals("txt"))
				{
					throw new CustomException(ExceptionsType.WRONG_FILE_TYPE,"enter proper extension");
				}	
				Files.write(Paths.get(FILE_PATH+TEXT_FILE), addressBookBuffer.toString().getBytes());
				System.out.println("Written in the file \n\n");
			}
			catch (NoSuchFileException e)
			{
				throw new CustomException(ExceptionsType.FILE_NOT_FOUND,"File Not Found");
			}
			catch (IOException e) 
			{
				throw new CustomException(ExceptionsType.FILE_NOT_FOUND,"File Not Found");
			}
		}

		//method to read data from file
		public void readFromTextFile() 
		{
			try
			{
				//reading data from file 
				String contentOfFile = Files.readString(Paths.get(FILE_PATH+TEXT_FILE));
				//printing data in console
				System.out.println(contentOfFile);
			}
			catch (NoSuchFileException e)
			{
				throw new CustomException(ExceptionsType.FILE_NOT_FOUND,"File Not Found");
			}
			catch (FileNotFoundException e) 
			{
				throw new CustomException(ExceptionsType.FILE_NOT_FOUND,"File Not Found");
			}
			catch (IOException e) 
			{
				System.err.println("Faced some problem while reading the file ");
			}
		}
		
		//method to create file if file doesn't exist
		private void checkFile() 
		{
			File file = new File(FILE_PATH+TEXT_FILE);
			try 
			{
				//checking file already exists
				if (!file.exists()) 
				{
					//if not creating a new file
					file.createNewFile();
					System.out.println("Created a file at "+FILE_PATH+TEXT_FILE);
				} 		
			}
			catch (IOException e1) 
			{			
				System.err.println("Problem encountered while creating a file");
			}
		}
		
		//method to write data into csv file
		public void writingToCsvFile() 
		{
			try
			{
				Writer writer = Files.newBufferedWriter(Paths.get(FILE_PATH+CSV_FILE)); //creating a file writer
				StatefulBeanToCsv<Contacts> beanToCsv = new StatefulBeanToCsvBuilder<Contacts>(writer)
						.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
						.build();
				List<Contacts> ContactList = new ArrayList<>(); //init empty contact list 
				addressBooks.entrySet().stream()
				.map(books->books.getKey())
				.map(bookNames->{ 
					return addressBooks.get(bookNames); 
				}).forEach(contacts ->{
					ContactList.addAll(contacts);
				});
				beanToCsv.write(ContactList);  //writing all the contact which is in contact list
				writer.close(); 
			}
			catch (NoSuchFileException e)
			{
				throw new CustomException(ExceptionsType.FILE_NOT_FOUND,"File Not Found");
			}
			catch (FileNotFoundException e) 
			{
				throw new CustomException(ExceptionsType.FILE_NOT_FOUND,"File Not Found");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (CsvDataTypeMismatchException e) 
			{				
				throw new CustomException(ExceptionsType.DATA_MISSMATCH,"data type missmatch");
			}
			catch (CsvRequiredFieldEmptyException e)
			{
				throw new CustomException(ExceptionsType.EMPTY_FILE,"problem while writing");
			}
		}

		//reading data from csv file
		public void readFromCsvFile() 
		{
			Reader reader;
			try {
				reader = Files.newBufferedReader(Paths.get(FILE_PATH+CSV_FILE));   //reader to read contacts
				CsvToBean<Contacts> csvToBean = new CsvToBeanBuilder<Contacts>(reader)
	                    .withType(Contacts.class)
	                    .withIgnoreLeadingWhiteSpace(true)
	                    .build();
				List<Contacts> contacts = csvToBean.parse();   //Converting them to list

				for(Contacts contact: contacts) {
				    System.out.println("Name : " + contact.getFirstName()+" "+contact.getLastname());
				    System.out.println("Email : " + contact.getEmail());
				    System.out.println("PhoneNo : " + contact.getPhoneNumber());
				    System.out.println("Address : " + contact.getAddress());
				    System.out.println("State : " + contact.getState());			    
				    System.out.println("City : " + contact.getCity());
				    System.out.println("Zip : " + contact.getZip());
				    System.out.println("==========================");
				}
			}
			catch (NoSuchFileException e)
			{
				throw new CustomException(ExceptionsType.FILE_NOT_FOUND,"File Not Found");
			}
			catch (FileNotFoundException e) 
			{
				throw new CustomException(ExceptionsType.FILE_NOT_FOUND,"File Not Found");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
}