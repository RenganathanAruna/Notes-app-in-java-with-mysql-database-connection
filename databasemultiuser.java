package notes;

import java.io.DataInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class databasemultiuser
{
	
    int count=0;
    String loginusername;
    public Connection con;
     
    
    
	public databasemultiuser() 
	{
		
		try 
		{
			con=DriverManager.getConnection("jdbc:mysql://localhost:2001/notesapp","renga","260701");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	public void register()
	{
		int j=0; 
		System.out.println("\n\nEnter the username to register: ");
		Scanner scan = new Scanner(System.in);
    	String regname = scan.nextLine();
    	System.out.println("\n\nEnter your password: ");
    	String password = scan.nextLine();
    	try {
    		
    		
    		PreparedStatement select=con.prepareStatement("select name from userdetails where name=?");
    		select.setString(1,regname);
            ResultSet rs = select.executeQuery();
    		while(rs.next())
    		{
    			j=1;
      		}
    	    
    		
    		if(j==0)
    		{
    	    PreparedStatement use=con.prepareStatement ("use notesapp");	
        	use.execute();
        	
        	
        	PreparedStatement insert=con.prepareStatement("insert into userdetails values(userid,?,?);");
        	insert.setString(1,regname);
        	insert.setString(2,password);
        	insert.executeUpdate();
        	
    		System.out.println("\nRegisteration successful");
    		
    		main1();
    		}
    		else
    		{
    			System.out.println("\nplease change the username there is already a registered user in this name");
    			register();
    		}
    		
    		}
   
    		
        catch (Exception e) 
        	{
                System.out.println("An error has occurred");
                System.out.println(e);
            }
    		
	}
	
	public void login()
	{
		int j=0,c=0;
		System.out.println("\n\nEnter your name to login: ");
		Scanner scan = new Scanner(System.in);
    	String logname = scan.nextLine();
    	
    	
    	System.out.println("\n\nEnter your password: ");
    	String password = scan.nextLine();
    	
    	
    	String str1,str2;
    	str1="shared";
    	str2=str1.concat(logname);
    	
    	try {
    		
    		PreparedStatement use=con.prepareStatement ("use notesapp");	
        	use.execute();
        	
        	PreparedStatement select=con.prepareStatement("select name from userdetails where name = ?");
        	select.setString(1, logname);
    		ResultSet rs = select.executeQuery();
    		
        	
    		while(rs.next())
    		{
    			j=1;
      		}
    		
    		if(j==1)
    		{

            	PreparedStatement select1=con.prepareStatement("select password from userdetails where name = ?");
            	select1.setString(1, logname);
            	ResultSet rs1 = select1.executeQuery();
            	rs1.next();
            	
        		String checkpassword =rs1.getString("password");
        			if(password.equals(checkpassword))
        			{
        				c=1;
        			  System.out.println("\n\nlogin successful");
        			  loginusername=logname;
        			  notesapp();
        			}
          		
    		}
    		else
    		{
    			System.out.println("\n\nplease register");
    			main1();
    		}
    		
    		if(c==0)
    		{
    			System.out.println("\n\nwrong password");
    			login();
    		}
    		

    		}
    	
    	catch (Exception e) 
    	{
            System.out.println("An error has occurred");
            System.out.println(e);
        }
	}
	
	
	
    public void createnote() 
    {
    	System.out.println("\n\nEnter the note name: ");
    	Scanner scan = new Scanner(System.in);
    	String name = scan.nextLine();
    	    	
    	int j=0;
	    char arr[][]=new char [100][100];
    	count = count+1;
        		
        	    try
        	    {	
        	     
        	    System.out.println("\nEnter the text that you want to add :\n");
        	    DataInputStream d = new DataInputStream(System.in);
        	    char ch;
        	    while((ch = (char)d.read())!='`')
        	    {
        	    	arr[count][j]=ch;
        	    	j=j+1;
        	    }
        	    }
        	   
        	    catch (IOException e )
        		{
                    System.out.println("An error has occurred");
                    
                }

             
     String content = String.copyValueOf(arr[count]);
     
     try {
 		
 		PreparedStatement use=con.prepareStatement ("use notesapp");	
    	use.execute();
    	
    	
    	
    	PreparedStatement select=con.prepareStatement("select *from userdetails  where name=?");
		select.setString(1,loginusername);
		ResultSet rs = select.executeQuery();
		rs.next();
		int userid = rs.getInt("userid");
    	
 		 
    	PreparedStatement insert=con.prepareStatement("insert into usernotes values (noteid,?,?,?);");
    	insert.setInt(1,userid);
    	insert.setString(2, name);
    	insert.setString(3, content);
    
 		int i=insert.executeUpdate();
 		
 		if(i>0)
 			System.out.println("\nnote created");
 		
 		else
 			System.out.println("\nnote not created");
 		}
 		
 		catch (Exception e) 
     	{
             System.out.println("An error has occurred");
             System.out.println(e);
         }
     
    }
    
    public void listnote()
    {
    	try {
    		
    		PreparedStatement use=con.prepareStatement ("use notesapp");	
        	use.execute();
        	

        	PreparedStatement select=con.prepareStatement("select *from userdetails  where name=?");
    		select.setString(1,loginusername);
    		ResultSet rs = select.executeQuery();
    		rs.next();
    		int userid = rs.getInt("userid");
        	
        	
        	
        	PreparedStatement select1=con.prepareStatement("select distinct notename from usernotes where userid =?");
        	select1.setInt(1, userid);
    		ResultSet rs1 = select1.executeQuery();
        	
    		System.out.println("\nThe list of notes avaliable are: \n");
    		while(rs1.next())
    		{
    			String display=rs1.getString("notename");
    			
    			System.out.println(display);
      		}
    		
    		
    		PreparedStatement select11=con.prepareStatement("select distinct notename from usersharednotes where receivername= ?");
    		select11.setString(1, loginusername);
    		ResultSet rs11 = select11.executeQuery();
        	
    		System.out.println("\nThe list of shared notes avaliable are: \n");
    		while(rs11.next())
    		{
    			String display1=rs11.getString("notename");
    			
    			System.out.println(display1);
      		}
        

    		}
    		
    	catch (Exception e) 
        	{
                System.out.println("\n\nThe list of notes in shared note are: ");
                System.out.println("\nThere is no shared note");
                System.out.println(e);
            }
    }
    
    
    
    public void listwithoutsharednote()
    {
    	try {
    		
    		PreparedStatement use=con.prepareStatement ("use notesapp");	
        	use.execute();
        	

        	PreparedStatement select=con.prepareStatement("select *from userdetails  where name=?");
    		select.setString(1,loginusername);
    		ResultSet rs = select.executeQuery();
    		rs.next();
    		int userid = rs.getInt("userid");
        	
        	
        	
        	PreparedStatement select1=con.prepareStatement("select distinct notename from usernotes where userid =?");
        	select1.setInt(1, userid);
    		ResultSet rs1 = select1.executeQuery();
        	
    		System.out.println("\nThe list of notes avaliable are: \n");
    		while(rs1.next())
    		{
    			String display=rs1.getString("notename");
    			
    			System.out.println(display);
      		}
    	}
    		
    	catch (Exception e) 
        	{
                System.out.println("\n\nThe list of notes in shared note are: ");
                System.out.println("\nThere is no shared note");
                System.out.println(e);
            }
    }
    
    
    
    
    
    public void viewnote()
    {
    	int j=0;
    	listnote();
    	System.out.println("\n\nEnter the note name to view: ");
    	Scanner scan = new Scanner(System.in);
    	String name = scan.nextLine();
    	
    	try 
    	{
    		
    		PreparedStatement use=con.prepareStatement ("use notesapp");	
        	use.execute();
     
        	
        	
        	PreparedStatement select=con.prepareStatement("select * from userdetails where name=?");
    		select.setString(1,loginusername);
    		ResultSet rs = select.executeQuery();
    		rs.next();
    		int userid = rs.getInt("userid");
    		

    		
        	PreparedStatement get=con.prepareStatement("select *from usernotes where userid=?");
    		get.setInt(1,userid);
    		ResultSet res = get.executeQuery();
    	
   		     while(res.next())
  		    {
   		    	 
    		  String checknote = res.getString("notename");
    	     
    		 if(checknote.equals(name))
    		    {
    			 String display = res.getString("notecontent"); 
    		     System.out.println("\nthe content of the note are: \n");
    			 System.out.println(display);   
    		     j=1;
    		     break;
    		    } 		     
            }	
    
       	}
    	catch (Exception e) 
    	   {
               j=0;          
           }
    	
    	if(j==0)
    	{
    	try 
    	{
    		
    		PreparedStatement use=con.prepareStatement ("use notesapp");	
        	use.execute();
        	
        
    		PreparedStatement select11=con.prepareStatement("select *from usersharednotes where receivername= ?");
    		select11.setString(1, loginusername);
    		ResultSet rs11 = select11.executeQuery();
        	
    		while(rs11.next())
    		{
    			
    			String checknotename=rs11.getString("notename");
    			
    			if(checknotename.equals(name))
    			{
                    String sendername=rs11.getString("sendername"); 
                    
                    

                	PreparedStatement select=con.prepareStatement("select *from userdetails  where name=?");
            		select.setString(1,sendername);
            		ResultSet rs = select.executeQuery();
            		rs.next();
            		int userid = rs.getInt("userid");
                    
                    
                  	
                  	PreparedStatement select1=con.prepareStatement("select notecontent from usernotes  where userid=?");
          			select1.setInt(1,userid);
              		ResultSet rs1 = select1.executeQuery();
              		
              		rs1.next();
              		String display1 = rs1.getString("notecontent");
              		
              		System.out.println("\nthe content of the note are: \n");
              		System.out.println(display1);
                      
                     break;
                     
    			}	
      		}
    		
    	}
    		
    	catch (Exception e) 
    	 {
                System.out.println("\nThere is no such note");
                System.out.println(e);
         }
    	
         }
    
    }
    
    
    
    
    public void deletenote()
    {
    	listwithoutsharednote();
    	System.out.println("\n\nEnter the note name to delete: ");
    	Scanner scan = new Scanner(System.in);
    	String name = scan.nextLine();
    	
    	try {
    		
    		PreparedStatement use=con.prepareStatement ("use notesapp");	
          	use.execute();
          	
          	PreparedStatement select=con.prepareStatement("select *from userdetails  where name=?");
    		select.setString(1,loginusername);
    		ResultSet rs = select.executeQuery();
    		rs.next();
    		int userid = rs.getInt("userid");
  
          	
    		PreparedStatement select1=con.prepareStatement("select  *from usernotes where userid=?");
    		select1.setInt(1,userid);
    		ResultSet rs1 = select1.executeQuery();
    		 		
    		
  	       while( rs1.next())
    		{
  	    	      String   checknamedelete = rs1.getString("notename");
    			  if(checknamedelete.equals(name))
    			  {
    				
    				String e=rs1.getString("notecontent");
    				
    				PreparedStatement delete1=con.prepareStatement("delete from usernotes where notecontent=?");
                    delete1.setString(1,e);
    				
    				int i=delete1.executeUpdate();
    	    		
    	    		if(i==1)
    	    		{
    	    			
    	    		System.out.println("\nthe note is deleted successfully");
    		        
    	    		}
    			}
    		}
  	     listwithoutsharednote();
  	     
  	     
  		PreparedStatement select11=con.prepareStatement("select *from usersharednotes where notename=?");
		select11.setString(1,name);
		ResultSet rs11 = select11.executeQuery();
		
		
		while(rs11.next())
		{
			String checkusername = rs11.getString("sendername");
			if(checkusername.equals(loginusername))
			{
				PreparedStatement delete1=con.prepareStatement("delete from usersharednotes where sendername=?");
                delete1.setString(1,checkusername);
                delete1.execute();
			}
		}
		
  	     
  	
    	}
    			
    	catch (Exception e) 
        	{
                System.out.println(e);
                
            }
    }
    
    
    public void searchnote()
    {
    	System.out.println("\n\nEnter the text to search: ");
    	Scanner scan = new Scanner(System.in);
    	String name = scan.nextLine();
    	
    	try {

    		PreparedStatement use=con.prepareStatement ("use notesapp");	
        	use.execute();
        	
        	PreparedStatement select=con.prepareStatement("select *from userdetails  where name=?");
    		select.setString(1,loginusername);
    		ResultSet rs = select.executeQuery();
    		rs.next();
    		int userid = rs.getInt("userid");

        	
        	
    		
        	PreparedStatement select1=con.prepareStatement("select *from usernotes where notename like '%"+name+"%'"); 
    		ResultSet rs1 = select1.executeQuery();

    		System.out.println("\nthe result of search are: \n");
    		int id;
    		while(rs1.next())
    		{
    			 id=rs1.getInt("userid");
    			if(id==userid)
    			{
    			String display=rs1.getString("notename");
    			System.out.println(display);
    			}
      		}

    		}
    		
    	catch (Exception e) 
        	{
                System.out.println("\nThere is no such note");
                System.out.println(e);
            }
    }
    
    
    public void sharenote()
    {
       int j=0;
       System.out.println("\n\nEnter the username you want to send the note: ");
       Scanner scan = new Scanner(System.in);
       String name = scan.nextLine();
       
       
   	
   	try
   	{

		PreparedStatement use=con.prepareStatement ("use notesapp");	
    	use.execute();
    	
    	PreparedStatement select=con.prepareStatement("select *from userdetails where name=?");
	    select.setString(1, name);
		ResultSet rs = select.executeQuery();
    	
   		while(rs.next())
   		{
   			j=1;
   			
   			if(name.equals(loginusername))
   				j=2;
     	}
   		
   		if(j==0)
   		{
   			System.out.println("\n\nNot registered user");
   			notesapp();
   		}
   		else if(j==2)
   		{
   			System.out.println("\n\nEntered your name please enter the user name you want to share");
   			sharenote();
   		}
   		else
   		{  			
   			listwithoutsharednote();
   			System.out.println("\n\nEnter the note name to share: ");
   	    	Scanner scann = new Scanner(System.in);
   	    	String notename = scann.nextLine();
   	    
   	       
   	    	try 
   	    	{
   	    	
   	    	PreparedStatement use1=con.prepareStatement ("use notesapp");	
   	       	use1.execute();
   	        	
   	        	 	
   	        PreparedStatement insert=con.prepareStatement("insert into usersharednotes values (?,?,?);");
   	     	insert.setString(1,loginusername);
   	     	insert.setString(2, name);
   	     	insert.setString(3, notename);
   	     
   	  		int i=insert.executeUpdate();
   	  		
   	  		if(i>0)
   	  			System.out.println("\nnote shared");
   	  		
   	  		else
   	  			System.out.println("\nnote not shared");
   	   	 	}
   	   	 		
   	   	 	
   	    	
   	    	catch (Exception e) 
   	     	{
   	             System.out.println("\n\nthere is no such note to share");
   	        }	
   		}
   	}
    catch (Exception e) 
	{
        System.out.println("An error has occurred");
        System.out.println(e);
    }
    	
 }
    
    
  
    
    public void notesapp()
	{
		int n;
		System.out.println("\n1.create note\n2.list note\n3.view note\n4.delete note\n5.search note\n6.share note\n7.back");
		Scanner scan = new Scanner(System.in);
		System.out.print("\nchoose any one option: ");
		n = 7;
		try
		{
		n = scan.nextInt();
		}
		catch ( java.util.InputMismatchException e) 
		{
	     System.out.println("\nplease enter the valid option :");
	     notesapp();
	    }
	       
		switch(n)
		{
		 case 1:createnote();
		        notesapp();

         case 2:listnote();
                notesapp();
	     
	     case 3:viewnote();
	            notesapp();
	     
	     case 4:deletenote();
	            notesapp();
	            
	     case 5:searchnote();
                notesapp();   
                
	     case 6:sharenote();
                notesapp();        
     
	     case 7: main1();
	    
	     default: System.out.println("\nEnter the valid option :");
	             notesapp();
	              
		}
	}
	
	
	
	
	public void main1()
	{
		int n;
		System.out.println("\n**Notes**");
		System.out.println("\n1.register\n2.login\n3.close app");
		Scanner scan = new Scanner(System.in);
		System.out.print("\nchoose any one option: ");
		n = 7;
		try
		{
		n = scan.nextInt();
		}
		catch ( java.util.InputMismatchException e) 
		{
	     System.out.println("\nplease enter the valid option :");
	     main1();
	    }
	       
		switch(n)
		{
		 case 1:register();
		     
		        
         case 2: login();
       
         
         case 3: System.out.println("bye..");
        	 try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        	 System.exit(0);
         
        	 
         default: System.out.println("\nEnter the valid option :");
         main1();
		}
	}
	
	
	public static void main(String[] args)
	{
		 
		databasemultiuser n = new databasemultiuser();
			n.main1();
	}	
}





