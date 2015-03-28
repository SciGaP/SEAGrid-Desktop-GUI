/*
 * Indentation is four.
 * This file does NOT use tabs.
 * Set your development tools appropriately.
 */

/*
 * Copyright (C) 2000, 2005 Scott Brozell
 * All Rights Reserved.
 *
 */
 
package org.gridchem.client;
 
import java.io.*; 
import org.gridchem.client.common.*; 


/**
 * A facility for tracing program execution.
 * Tracing output is sent to System.err.
 * Tracing output is tagged with Trace: at the beginning of a line.
 * This is followed by indentation to show the function nesting level.
 * Next the type of tracing output is indicated via Entry, Exit, or Note.
 * Then the function name, file name, and line number are emitted.
 * Finally for trace Note type output, the note is outputted.
 *
 * Because Java does not have destructors and finalize methods are not
 * guaranteed to be called, this class has only static methods and should
 * be used as a singleton.
 * Example usage:
 *public class TraceTest
 *{
 *   public static void main(String[] args)
 *   {
 *      Trace.entry( ) ;
 *      Trace.note( "Number of arguments = " + args.length ) ;
 *      if ( args.length < 2 ) {
 *         Trace.traceOn( ) ;
 *         System.out.println( "Normal output." ) ;
 *      }
 *      else {
 *         Trace.traceOff( ) ;
 *         System.out.println( "Abnormal output." ) ;
 *      }
 *      Trace.exit( ) ;
 *      System.exit( 0 ) ;
 *   }
 *}
 *
 * Example output:
 *java -classpath "." TraceTest
 *Trace: Entry at TraceTest.main(TraceTest.java:5)
 *Trace:   Note at TraceTest.main(TraceTest.java:6)  Number of arguments = 0
 *Normal output.
 *Trace: Exit at TraceTest.main(TraceTest.java:15)
 *
 *java -classpath "." TraceTest 1 2 3
 *Trace: Entry at TraceTest.main(TraceTest.java:5)
 *Trace:   Note at TraceTest.main(TraceTest.java:6)  Number of arguments = 3
 *Abnormal output.
 *
 * The initial version was 1.00 and was implemented in Java 1.1.
 * Now CVS information is used.
 * @author Scott Brozell
 * @created 23 Mar 2000
 * @version $Revision: 1.5 $
 * @date $Date: 2005/11/18 22:22:32 $ modified by $Author: srb $
 */

public class Trace {

    /**
     * If tracing is on, log entry to the caller's method.
     */
    public static void entry() { 
        if ( isTracingOn() ) {
            System.err.println( preamble() + " Entry" + getCallerInfo() ) ;
            indentation_increment() ;
        } 
    } 

    /**
     * If tracing is on, log exit from the caller's method.
     */
    public static void exit() { 
        if ( isTracingOn() ) {
            indentation_decrement() ;
            System.err.println( preamble() + " Exit" + getCallerInfo() ) ;
        } 
    } 

    /**
     * Is tracing on ?
     * @return    true if tracing is on.
     */
    public static boolean isTracingOn() { 
        return tracingOn ;
    } 

    /**
     * If tracing is on, log the caller's message.
     * @param    String     a trace message.
     */
    public static void note( String message ) { 
        if ( isTracingOn() ) {
            System.err.println( preamble() + " Note" + getCallerInfo() +
                                "  " + message ) ;
        } 
    } 

    /**
     * Print a stack trace
     */
    public static void printStackTrace() {
        if ( isTracingOn() ) {
            new Throwable().printStackTrace( ) ; 
        } 
    } 

    /**
     * Turn tracing on or off.
     * @param    boolean    true turns tracing on.
     */
    public static void trace( boolean on ) { 
        tracingOn = on ;
    } 

    /**
     * Turn tracing off.
     */
    public static void traceOff() { 
        tracingOn = false ;
    } 

    /**
     * Turn tracing on.
     */
    public static void traceOn() { 
        tracingOn = true ;
    } 

    /**
     * Helper method that produces the caller's name and source information.
     * An example stack trace with java -debug:
     * java.lang.Throwable
     *    at Trace.getCallerInfo(Trace.java:77)
     *    at Trace.exit(Trace.java:31)
     *    at Test.main(Test.java:11)
     * An example stack trace without debug:
     * java.lang.Throwable
     *    at java.lang.Throwable.<init>(Compiled Code)
     *    at Trace.getCallerInfo(Compiled Code)
     *    at Trace.exit(Compiled Code)
     *    at Test.main(Compiled Code)
     * @return    the source name and info as given in the stack trace.
     */
    private static String getCallerInfo() { 
        final String thisMethodName = "getCallerInfo" ;
        StringWriter sw = new StringWriter() ; 
        new Throwable().printStackTrace( new PrintWriter( sw ) ) ; 
        String stackTrace = sw.getBuffer().toString() ; 
        BufferedReader sr = new BufferedReader( new StringReader( stackTrace )); 
        try {         
            String line; 
            while( ( line = sr.readLine() ) != null ) { 
                if( line.indexOf( thisMethodName ) != -1 ) {
                    // found getCallerInfo in the stack trace
                    sr.readLine() ;  // skip the calling Trace routine
                    line = sr.readLine() ;
                    return " " + line.trim() ;
                } 
            } 
        } 
        catch( IOException ioe ) { 
            System.err.println( "Error reading the stack trace !" ) ;
        } 
        return "Warning: Unidentified Method" ;
    } 

    /**
     * @return    the nesting level indentation for the log output.
     */
    private static String indent() { 
        String indentation = "" ; 
        for ( int i = 1; i <= indentation_level; ++ i ) {
            indentation += indentation_spacing ;
        }
        return indentation ;
    } 

    /**
     * Decreases the indentation level for the log output.
     */
    private static void indentation_decrement() { 
        -- indentation_level ;
        //ASSERT( indentation_level >= 0 ) ;
    } 

    /**
     * Increases the indentation level for the log output.
     */
    private static void indentation_increment() { 
        ++ indentation_level ;
        //ASSERT( indentation_level > 0 ) ;
    } 

    /**
     * Produces the premable for the log output.
     * The preamble is the output tag and the nesting indentation.
     * @return    the preamble.
     */
    private static String preamble() { 
        return output_tag + indent() ;
    } 

    /**
     * Defines the current nesting level of indentation.
     */
    private static int indentation_level = 0 ;

    /**
     * All Trace output is indented to indicate the nesting level.
     * Defines the unit of indentation.
     */
    private static final String indentation_spacing = "  " ; 

    /**
     * All Trace output contains the output tag.
     */
    private static final String output_tag = "Trace:" ;

    private static boolean tracingOn = Settings.DEBUG ;
}

