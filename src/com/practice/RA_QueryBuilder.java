/**
 * 
 */
package com.practice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author raror3
 *
 */
public class RA_QueryBuilder {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File queryFile = new File("D:/queryBuilderData/query.txt");
		FileReader fileReader = new FileReader(queryFile);
		File dataFile = new File("D:/queryBuilderData/data.txt");
		FileReader dataReader = new FileReader(dataFile);
		
		BufferedReader bufferedQueryReader = new BufferedReader(fileReader);
		BufferedReader bufferedDataReader = new BufferedReader(dataReader);
		
		List<String> list = new ArrayList<>();

		try (Stream<String> stream = Files.lines(Paths.get("D://queryBuilderData//query.txt"))) {

			//1. filter line 3
			//2. convert all content to upper case
			//3. convert it into a List
			list = stream
					.map(String::toUpperCase)
					.collect(Collectors.toList());
			
			String queryStart = list.get(0);
			String queryEnd = list.get(1);
			
			int finalQueryMaxLimit = 2499 - (queryStart.length() + queryEnd.length());
			
			/*list.forEach(item->{
				String queryStart = item;
				String queryEnd = queryFileIterator.next();
			});*/

			
			
			List<String> finalQueryList = new ArrayList<String>(1);
			StringBuilder finalQuery = new StringBuilder(queryStart);
			createQueriesInFile(queryStart, queryEnd, finalQueryList, finalQuery);
			System.out.println(finalQueryList.toString());
			writeToFile(finalQueryList);
		}
	}

	/**
	 * @param queryEnd
	 * @param finalQuery
	 * @throws IOException
	 */
	public static void createQueriesInFile(String queryStart, String queryEnd, List<String> finalQueryList, StringBuilder finalQuery)
			throws IOException {
		Stream<String> dataFileStream = Files.lines(Paths.get("D://queryBuilderData//data.txt"));
		List<String> dataList = new ArrayList<>();
		dataList = dataFileStream
				.map(String::toUpperCase)
				.collect(Collectors.toList());
		
		dataList.forEach(item->{
			if (finalQuery.length() + item.length() + queryEnd.length() + 3 < 2499) {
				finalQuery.append("'").append(item).append("',");
			} else if (finalQuery.length() + item.length() + queryEnd.length() + 3 >= 2499) {
				//System.out.println("Length: " + finalQuery.length());
				//System.out.println("CharAt: " + finalQuery.charAt(finalQuery.length()-1));
				if (finalQuery.charAt(finalQuery.length()-1) == ',') {
					finalQuery.replace(finalQuery.length()-1, finalQuery.length(), "");
					finalQuery.append(queryEnd);
				}
				finalQueryList.add(finalQuery.toString());
				finalQuery.delete(0, finalQuery.length());
				//finalQuery.append("\n");
				finalQuery.append(queryStart);
				finalQuery.append("'").append(item).append("'");
			} else {
				finalQuery.append(queryEnd);
				return;
			}
		});
		finalQuery.replace(finalQuery.length()-1, finalQuery.length(), "");
		finalQuery.append(queryEnd);
	}

	private static void writeToFile(List<String> finalQueryList) throws IOException {
		File queryOutputFile = new File("D:/queryBuilderData/queries.txt");
		FileWriter fileWriter = new FileWriter(queryOutputFile);
		for (String finalQuery : finalQueryList) {
			fileWriter.write(finalQuery);
			fileWriter.write("\n");
			fileWriter.flush();
		}
		fileWriter.close();
	}
}
