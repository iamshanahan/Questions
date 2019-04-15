package questions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class FilePipeDoer {

	public static void main( String args[] ) throws IOException {
		Reader reader = null;
		Writer writer = null;
		try {
			switch( args.length ) {
			case 0:
				reader = new InputStreamReader( System.in );
				writer = new PrintWriter( System.out );
				break;
			case 1:
				reader = new FileReader( args[0] );
				writer = new PrintWriter( System.out );
			default:
				throw new AvmException( "Too many command-line arguments" );
			}
		} catch( Exception e ) {
			e.printStackTrace();
			System.err.println( "Usage: <program> [inputfile] [outputfile]" );
			System.exit(1);
		}
		System.err.println( "Processing..." );
		List<String> data = read( reader );
		reader.close();
		write( writer, data );
		writer.flush();
		writer.close();
		
	}

	private static List<String> read(Reader reader) throws IOException {
		BufferedReader bReader = new BufferedReader( reader );
		List<String> data = new ArrayList<>();
		String line = bReader.readLine();
		while( line != null ) {
			data.add( line );
			line = bReader.readLine();
		}
		return data;
	}

	private static void write(Writer writer, List<String> data) throws IOException {
		for( String line : data ) {
			writer.write( line );
			writer.write( '\n' );
		}	
	}
}
