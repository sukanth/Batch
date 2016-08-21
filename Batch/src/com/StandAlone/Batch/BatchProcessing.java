package com.StandAlone.Batch;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.*;
 
public class BatchProcessing {
    
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JLabel statusLabel;
   private JPanel controlPanel;

   public BatchProcessing(){
      prepareGUI();
   }

   public static void main(String[] args){
      BatchProcessing  swingControl = new BatchProcessing();      
      swingControl.copyFiles();
   }

   /**
    * 
    */
   private void prepareGUI(){
      mainFrame = new JFrame("MOVE FILES FROM SOURCE FOLDER TO DESTINATION FOLDER");
      mainFrame.setSize(1000,1000);
      mainFrame.setLayout(new GridLayout(3, 1));
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
      headerLabel = new JLabel("", JLabel.CENTER);        
      statusLabel = new JLabel("",JLabel.CENTER);    

      statusLabel.setSize(350,100);

      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      mainFrame.setVisible(true);  
   }
   
   /**@author sukanthgunda
    * @param sourceFolder,destinationFolder
    * @throws IOException 
    * @return noOfFilesMoved
    */
   public static int moveFiles(File sourceFolder,File destinationFolder) throws IOException{
	int noOfFilesMoved = 0;
	boolean isFolderCreated = false;
	InputStream inStream = null;
	OutputStream outStream = null;
	if(!destinationFolder.exists()){
	    isFolderCreated =  destinationFolder.mkdir();
	    if(!isFolderCreated){
		JOptionPane.showMessageDialog(null, "******* DESTINATION FOLDER NAME NOT VALID ************", "Failure", JOptionPane.ERROR_MESSAGE);
	    }
	}
	File[] sourceFiles = sourceFolder.listFiles();
	for(File x : sourceFiles){
	    if (x.isFile()) {
		try {
		    inStream = new FileInputStream(x);
		    outStream = new FileOutputStream(new File(destinationFolder +File.separator+x.getName()));
		    byte[] buffer = new byte[1024];
		    int length;
	    	    //copy the file content in bytes 
	    	    while ((length = inStream.read(buffer)) > 0){
	    	    	outStream.write(buffer, 0, length);
	    	    }
	    	    System.out.println(noOfFilesMoved +"--"+x.getName() + "-- Successfully moved to Destination");
	    	    inStream.close();
	    	    outStream.close();
	    	    //delete the original file
	    	    x.delete();
	    	  noOfFilesMoved++;
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
	    }
	}
	return noOfFilesMoved;
   }

   /**
    * @author sukanthgunda
    * @param folderName
    * @return noOfFiles
    */
   public static int getNoOfFileInFolder(File folderName){
	int noOfFiles = 0;
	try {
		// TODO Auto-generated method stub
		File[] files = folderName.listFiles();
		for (File x : files) {
		    noOfFiles++;
			 if (x.isFile()) {
			 System.out.println(noOfFiles+" "+x.getName());
			}
		}
	} catch (Exception e) {
		System.out.println("file not found " + e);
	}
	return noOfFiles;
   }
  
   /**
    * 
    */
   private void copyFiles(){
      String noFilesInSourceFolder = null;
      headerLabel.setText("Batch Process Files"); 

      JLabel  sourcePath = new JLabel("Enter Source Path: ", JLabel.CENTER);
      JLabel  destinationPath = new JLabel("Enter Destination Path: ", JLabel.CENTER);
      final JTextField sourceText = new JTextField(50);
      final JTextField destinationText = new JTextField(50);     

      JButton moveFilesButton = new JButton("Move Files");
      moveFilesButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {     
            String srcPath = sourceText.getText().trim();
            String destPath = destinationText.getText().trim();
           
              	File sourceFolder = new File(srcPath);
        	File destinationFolder = new File(destPath);
        	if(sourceFolder.exists()){
        	int filesInSourceFolder=getNoOfFileInFolder(sourceFolder);
        	if(filesInSourceFolder < 1){
        	    JOptionPane.showMessageDialog(null, "******* No Files In Source Folder ************", "Failure", JOptionPane.ERROR_MESSAGE);
        	}
        	System.out.println("NO OF FILES AVAILABLE IN GIVEN SOURCE FOLDER ARE " + filesInSourceFolder);
		    try {
			int noOfFilesMoved = moveFiles(sourceFolder,destinationFolder);
			System.out.println("No OF FILES MOVED TO DESTINATION FOLDER SUCCESSFULLY ARE "+ noOfFilesMoved);
		    } catch (IOException e1) {
			e1.printStackTrace();
		    }
		}
        	else{
        	    JOptionPane.showMessageDialog(null, "******* Source folder doesnot exists or not valid ************", "Failure", JOptionPane.ERROR_MESSAGE);
        	}
         }
      }); 
      controlPanel.add(sourcePath);
      controlPanel.add(sourceText);
      controlPanel.add(destinationPath);
      controlPanel.add(destinationText);
      controlPanel.add(moveFilesButton);
      mainFrame.setVisible(true);  
   }
}