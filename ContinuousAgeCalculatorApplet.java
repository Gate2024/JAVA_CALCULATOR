    import java.applet.Applet;
    import java.awt.*;
    import java.awt.event.*;
    import java.util.Calendar;

    public class ContinuousAgeCalculatorApplet extends Applet implements ActionListener, Runnable
           	 {
           private TextField dobField, startDateField, endDateField;
           private TextArea resultArea;
           private Thread timerThread;
           private Calendar dob;

        public void init() 
                             {

            setLayout(new BorderLayout());
            setBackground(new Color(100, 100, 100));

             // Header
                Label titleLabel = new Label("Age Calculator", Label.CENTER);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
                titleLabel.setForeground(new Color(100, 0, 100));
                add(titleLabel, BorderLayout.NORTH);

              // Main Panel
                 Panel mainPanel = new Panel();
                 mainPanel.setLayout(new GridLayout(5, 2));
                 mainPanel.setBackground(new Color(100, 100, 100));

                // Date of Birth Input
                   Label dobLabel = new Label("Enter Your Date Of Birth (DD-MM-YYYY): ");
                   dobField = new TextField(20);
                   dobField.setFont(new Font("Arial", Font.BOLD, 25));

                  // Start Date Input
                     Label startDateLabel = new Label("Start Date (DD-MM-YYYY): ");
                     startDateField = new TextField(20);
                     startDateField.setFont(new Font("Arial", Font.BOLD, 25));

                    // End Date Input
                       Label endDateLabel = new Label("End Date (DD-MM-YYYY): ");
                       endDateField = new TextField(20);
                       endDateField.setFont(new Font("Arial", Font.BOLD, 25));

                      // Calculate Button
                         Button calculateButton = new Button("Calculate Age:");
                         calculateButton.setFont(new Font("Arial", Font.BOLD, 35));
        		 calculateButton.setBackground(new Color(20, 5, 20));
        		 calculateButton.setForeground(Color.WHITE);
       			 calculateButton.addActionListener(this);

       			 // Result Area
        		    resultArea = new TextArea("Your age will be displayed here.", 10, 20, TextArea.SCROLLBARS_NONE);
        		    resultArea.setFont(new Font("Arial", Font.PLAIN, 25));
        		    resultArea.setEditable(false);
       		            resultArea.setBackground(new Color(200, 200, 200));

       			    // Add components to main panel
       			       mainPanel.add(dobLabel);
         		       mainPanel.add(dobField);
       			       mainPanel.add(startDateLabel);
        		       mainPanel.add(startDateField);
                               mainPanel.add(endDateLabel);
                               mainPanel.add(endDateField);
      			       mainPanel.add(calculateButton);

        		        // Add panels to applet
       				   add(mainPanel, BorderLayout.CENTER);
       				   add(resultArea, BorderLayout.SOUTH);
   			 }

          			 @Override
   		 		    public void actionPerformed(ActionEvent e) {
                                    boolean dobProvided = false;

       				    // Calculate age from Date of Birth
        			       String dobInput = dobField.getText();
       				       String[] dobParts = dobInput.split("-");

       					 if (dobParts.length == 3) {
          				  try {
                                                int day = Integer.parseInt(dobParts[0]);
                                                int month = Integer.parseInt(dobParts[1]) - 1; // Months are 0-indexed
                				int year = Integer.parseInt(dobParts[2]);
                				dob = Calendar.getInstance();
               				        dob.set(year, month, day);
              				        dobProvided = true;

                                                      // Start the timer thread if not already running
                                                      if (timerThread == null || !timerThread.isAlive()) 
                                                          {
                                                         timerThread = new Thread(this);
                                                         timerThread.start();
               					          }
                                              } 
                                         catch (NumberFormatException ex) {
              			               resultArea.setText("Invalid DOB Format. Use DD-MM-YYYY.");
                                               return;
           								   }
        							    }

         						// Calculate age between start and end dates
        						   String startDateInput = startDateField.getText();
       							   String endDateInput = endDateField.getText();
       							   String[] startParts = startDateInput.split("-");
      							   String[] endParts = endDateInput.split("-");

         				  if (startParts.length == 3 && endParts.length == 3) {
           					 try {
            	 					Calendar startDate = Calendar.getInstance();
                					Calendar endDate = Calendar.getInstance();

               					        int startDay = Integer.parseInt(startParts[0]);
                					int startMonth = Integer.parseInt(startParts[1]) - 1;
               					        int startYear = Integer.parseInt(startParts[2]);
                   				        startDate.set(startYear, startMonth, startDay);

                					int endDay = Integer.parseInt(endParts[0]);
                					int endMonth = Integer.parseInt(endParts[1]) - 1;
              					        int endYear = Integer.parseInt(endParts[2]);
                 					endDate.set(endYear, endMonth, endDay);

                						if (startDate.after(endDate)) { 
                                                                   resultArea.setText("Start date cannot be after end date.");
                                                                   return;
              										      }
	
             							   // Calculate age between start and end dates
                						      updateAgeBetweenDates(startDate, endDate);
         								                         } 
	 					catch (NumberFormatException ex) {
               					       resultArea.setText("Invalid Date Format. Use DD-MM-YYYY.");
          									  }
        						  } else {
           							 resultArea.setText("Invalid Start/End Date Format. Use DD-MM-YYYY.");
       								 }

      							  // If DOB was not provided, only calculate the age between the dates
       								 if (!dobProvided) {
          						         resultArea.append("\nNo Date of Birth provided.");
      									            }
 									   }

   							 private void updateAge() {
    							    if (dob != null) {
       						            Calendar now = Calendar.getInstance();
            						    int years = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
             						    int months = now.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
         						    int days = now.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);

          						    if (days < 0) {
              							  months--;
              							  now.add(Calendar.MONTH, -1);
             						          days += now.getActualMaximum(Calendar.DAY_OF_MONTH);
           								  }	

          						  if (months < 0) {
           						      years--;
              					              months += 12;
           							          }

            					          long seconds = (now.getTimeInMillis() - dob.getTimeInMillis()) / 1000;
                                                          long hours = seconds / 3600;
                                                          long minutes = (seconds % 3600) / 60;
                                                          seconds = seconds % 60;

                                                          String resultText = String.format("Age from DOB:\n%d years\n%d months\n%d days\n%d hours\n%d minutes\n%d seconds",
                                                          years, months, days, hours, minutes, seconds);
                                                          resultArea.setText(resultText);
                                                                                        }
                                                                                   }

      							  private void updateAgeBetweenDates(Calendar startDate, Calendar endDate) {
         						  int years = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
      							  int months = endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
       							  int days = endDate.get(Calendar.DAY_OF_MONTH) - startDate.get(Calendar.DAY_OF_MONTH);

       							 if (days < 0) {
          					         months--;
           						 endDate.add(Calendar.MONTH, -1);
            						 days += endDate.getActualMaximum(Calendar.DAY_OF_MONTH);
      								  }

       							 if (months < 0) {
        					         years--;
            					         months += 12;
      									  }

        						 String resultText = String.format("Age between dates:\n%d years\n%d months\n%d days",
               						 years, months, days);
       							 resultArea.append("\n" + resultText);
    									}

    							 public void run() {
     								   while (true) {
           								 try {
               								     Thread.sleep(1000); // Update every second
                							     updateAge();
            								     }
           								 catch (InterruptedException e) {
              										  break;
            												}
      												  }
   											  }

  									  public void destroy() {
          								  if (timerThread != null) {
               							          timerThread.interrupt();
      											           }
  											        }
											     }
