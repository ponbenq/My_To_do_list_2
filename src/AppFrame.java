import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class AppFrame extends JFrame implements ActionListener {
    private JButton addButton, deleteButton, show,showPoint, sortBtn;
    private JList list;
    private DefaultListModel listModel;
    private JTextField text,dueDate;
    private LinkedList linkedList = new LinkedList();
    private TaskLinkedList taskLinkedList = new TaskLinkedList();
    private JScrollPane scrollPane;
    private JLabel logoBand, inputTaskLabel,dueDateLabel;
    public AppFrame() throws Exception {
        //frame init
        this.setSize(440, 445);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Doodle-List");
        this.getContentPane().setBackground(new Color(171, 206, 215));

        //label
        inputTaskLabel = new JLabel("input Task :");
        inputTaskLabel.setBounds(20, 100, 100,25);
        add(inputTaskLabel);

        //due date label
        dueDateLabel = new JLabel("Due Date :");
        dueDateLabel.setBounds(300, 100, 100, 25);
        add(dueDateLabel);

        //text field
        text = new JTextField();
        text.setBounds(15, 80+40, 280, 25);
        add(text);

        //duedate field
        dueDate = new JTextField();
        dueDate.setBounds(295, 120, 125, 25);
        add(dueDate);

        //add button
        addButton = new JButton(" + ADD ");
        addButton.setBounds(20, 160, 90, 25);
        addButton.setBackground ( Color.BLACK );
        addButton.setForeground ( Color.black );
        addButton.setBorder ( BorderFactory.createLineBorder ( Color.black, 2 ) );
        addButton.addActionListener(this);
        add(addButton);

        //delete button
        deleteButton = new JButton(" - DELETE ");
        deleteButton.setBackground ( Color.BLACK );
        deleteButton.setForeground ( Color.black );
        deleteButton.setBorder ( BorderFactory.createLineBorder ( Color.black, 2 ) );
        deleteButton.setBounds(130, 160, 90, 25);
        deleteButton.addActionListener(this);
        add(deleteButton);

        //list
        listModel = new DefaultListModel();
        list = new JList(listModel);
        list.setBounds(20, 260, 545, 200);
        list.setVisibleRowCount(12);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(list);
        scrollPane.setBounds(20, 205, 400, 200);
        add(scrollPane);

        //Add Picture
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("DDT.png").getImage().getScaledInstance(350/2, 150/2, Image.SCALE_DEFAULT));
        this.setIconImage(imageIcon.getImage());
        logoBand = new JLabel("", (Icon) imageIcon,JLabel.CENTER);
        logoBand.setBounds(120,15,((Icon) imageIcon).getIconWidth(),((Icon) imageIcon).getIconHeight());
        add(logoBand);

        //load all list to JList
        loadList();

        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setResizable(false);
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        try {
            if (e.getSource() == addButton) {
                String inputText = text.getText();

                //Create a Date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //get Current Date and time
                Date date = new Date();
                //combine format and string
                String formattedDate = dateFormat.format(date);
                String combineText = inputText + ": " + formattedDate;

                //localdate
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String dueDateS = dueDate.getText();
                LocalDate localDateTemp = LocalDate.parse(dueDateS, formatter);

                //put the input to file
                File file = new File("allList/"+LoginFrame.folderName+"/list_"+LoginFrame.folderName+".txt");
                Scanner scanner = new Scanner(file);
                String originalString = "";

                while(scanner.hasNextLine()){
                    originalString += scanner.nextLine()+"\n";
                }

                scanner.close();
                PrintStream printStream = new PrintStream(file);
                printStream.println(originalString + inputText + "," + LocalDate.now() + "," + localDateTemp);

                //add data to linked list
                Task temp;
                temp = new Task(inputText, LocalDate.now(),localDateTemp);
                taskLinkedList.insert(temp);
                sorted();
            } else if (e.getSource() == deleteButton) {
                int index = list.getSelectedIndex();
                if (index != -1) {
                    //get text from 
                    String text = (String)listModel.get(index);
                    System.out.println(text);

                    //convert to Object type
                    String []subText = text.split(",");
                    String iterSubText = subText[0];
                    System.out.println("sub text is :"+iterSubText);
                    Object obj = convertNameToTask(iterSubText);

                    //add data to each node and JList
                    taskLinkedList.delete(obj);
                    listModel.remove(index);

                    //remove in file
                    Path filePath = Paths.get("allList/"+LoginFrame.folderName+"/list_"+LoginFrame.folderName+".txt");
                    List<String> lines = Files.readAllLines(filePath);
                    for(int i = 0 ;i < lines.size(); i++){
                        String []subString = lines.get(i).split(",");
                        if(subString[0].equals(iterSubText)){
                            lines.remove(i);
                        }
                    }
                    Files.write(filePath, lines, StandardOpenOption.TRUNCATE_EXISTING);
                }
            } else if (e.getSource() == show) {
                if (taskLinkedList.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "List Empty!");
                } else {
                    Object obj = taskLinkedList.retrieve();
                    Task temp;
                    temp = (Task) obj;
                    JOptionPane.showMessageDialog(null, temp.getTaskTitle());
                }
            } else if (e.getSource() == showPoint) {
                int index = list.getSelectedIndex();
                if(index != -1){
                    JOptionPane.showMessageDialog(null, listModel.get(index));
                }
            } else if (e.getSource() == sortBtn) {
                //sort here
            }
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    public void loadList(){
        try {
            File listDirectory = new File("allList/"+LoginFrame.folderName);
            if(!listDirectory.exists()){
                //create directory
                listDirectory.mkdir();

                //create new file in recent created directory
                File listFile = new File(listDirectory, "/list_" + LoginFrame.folderName + ".txt");
                listFile.createNewFile();
            }else{
                //read data from file
                Path filePath = Paths.get("allList/"+LoginFrame.folderName+"/list_"+LoginFrame.folderName+".txt");
                List<String> lines = Files.readAllLines(filePath);
                System.out.println(lines.size());
                if(lines.size() != 0) {
                    for (int i = 0; i < lines.size(); i++) {
                        if (lines.get(i) != "\n" && lines.get(i) != " ") {
                            Task temp;
                            String substring[] = lines.get(i).split(",");
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate s1 = LocalDate.parse(substring[1], formatter);
                            LocalDate s2 = LocalDate.parse(substring[2], formatter);
                            temp = new Task(substring[0],s1,s2);
                            taskLinkedList.insert(temp);
                        }
                    }
                    sorted();
                }
            }
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void sorted(){
        try {
            //loop to get all task in linked list
            ArrayList<Task> tasks = new ArrayList<>();
            taskLinkedList.findFirst();
            while (taskLinkedList.current != null) {
                Task task = (Task)taskLinkedList.current.getData();
                tasks.add(task);
                taskLinkedList.current = taskLinkedList.current.getNextNode();
            }
            //then calculate different between due dat and input date of each node
            int[] dateDiff = new int[tasks.size()];
            for(int i = 0;i < tasks.size(); i++){
                int days = Period.between(tasks.get(i).getInputDate(),tasks.get(i).getDueDate()).getDays();
                dateDiff[i] = days;
            }
            Arrays.sort(dateDiff);
            System.out.println(Arrays.toString(dateDiff));

            //set Node empty
            taskLinkedList.first = null;
            taskLinkedList.current = null;

            //remove JList element
            listModel.removeAllElements();
            for(int i = 0;i < tasks.size();i++){
                for(Task task : tasks){
                    int days = Period.between(task.getInputDate(), task.getDueDate()).getDays();
                    if(days == dateDiff[i]){
                        //insert to node
                        taskLinkedList.insert(task);

                        //add to JList
                        String title = ((Task) taskLinkedList.current.getData()).getTaskTitle();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String createDate = formatter.format(((Task) taskLinkedList.current.getData()).getInputDate());
                        String dueDate = formatter.format(((Task) taskLinkedList.current.getData()).getDueDate());

                        //add element to JList in order => title of task, input Date, due date
                        listModel.addElement(title+","+createDate+","+dueDate);
                        break;
                    }
                }
            }
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public Task convertNameToTask(String taskName){
        try {
            //loop to get all Task of linked list
            ArrayList<Task> tasks = new ArrayList<>();
            taskLinkedList.findFirst();
            while(taskLinkedList.current != null){
                Task task = ((Task)taskLinkedList.current.getData());
                tasks.add(task);
                taskLinkedList.current = taskLinkedList.current.getNextNode();
            }
            //find specific task by argument taskName
            for(Task task : tasks){
                if(task.getTaskTitle().equals(taskName)){
                    return task;
                }
            }
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return null;
    }
}
