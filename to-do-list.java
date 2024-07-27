import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class AdvancedToDoList extends JFrame {
    private List<Task> tasks;
    private DefaultListModel<Task> listModel;
    private JList<Task> taskList;

    public AdvancedToDoList() {
        tasks = new ArrayList<>();
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);

        setTitle("Advanced To-Do List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 400));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("To-Do List");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(taskList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 5, 0));

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskDialog taskDialog = new TaskDialog(AdvancedToDoList.this);
                taskDialog.setVisible(true);
                Task newTask = taskDialog.getTask();
                if (newTask != null) {
                    tasks.add(newTask);
                    listModel.addElement(newTask);
                }
            }
        });
        buttonPanel.add(addButton);

        JButton deleteButton = new JButton("Delete Task");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedIndices = taskList.getSelectedIndices();
                for (int i = selectedIndices.length - 1; i >= 0; i--) {
                    int selectedIndex = selectedIndices[i];
                    tasks.remove(selectedIndex);
                    listModel.remove(selectedIndex);
                }
            }
        });
        buttonPanel.add(deleteButton);

        JButton markCompleteButton = new JButton("Mark as Complete");
        markCompleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedIndices = taskList.getSelectedIndices();
                for (int selectedIndex : selectedIndices) {
                    tasks.get(selectedIndex).setCompleted(true);
                    listModel.setElementAt(tasks.get(selectedIndex), selectedIndex);
                }
            }
        });
        buttonPanel.add(markCompleteButton);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = JOptionPane.showInputDialog("Enter search query:");
                if (searchQuery != null && !searchQuery.isEmpty()) {
                    List<Task> searchResults = searchTasks(searchQuery);
                    displayTasks(searchResults);
                }
            }
        });
        buttonPanel.add(searchButton);

        JButton sortButton = new JButton("Sort by Priority");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Collections.sort(tasks, Comparator.comparingInt(Task::getPriority).reversed());
                updateListModel();
            }
        });
        buttonPanel.add(sortButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void updateListModel() {
        listModel.clear();
        for (Task task : tasks) {
            listModel.addElement(task);
        }
    }

    private void displayTasks(List<Task> taskList) {
        listModel.clear();
        for (Task task : taskList) {
            listModel.addElement(task);
        }
    }

    private List<Task> searchTasks(String query) {
        List<Task> searchResults = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(task);
            }
        }
        return searchResults;
    }

    public static void main(String[] args) {
        new AdvancedToDoList();
    }

    static class Task {
        private String description;
        private boolean completed;
        private int priority;

        public Task(String description, int priority) {
            this.description = description;
            this.priority = priority;
            this.completed = false;
        }

        public String getDescription() {
            return description;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public int getPriority() {
            return priority;
        }

        @Override
        public String toString() {
            return description + (completed ? " (Completed)" : "");
        }
    }

    static class TaskDialog extends JDialog {
        private Task task;
        private JTextField descriptionField;
        private JSpinner prioritySpinner;
        private JButton okButton;
        private JButton cancelButton;

        public TaskDialog(Frame parent) {
            super(parent, "Add Task", true);
            initialize();
        }

        private void initialize() {
            JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));

            panel.add(new JLabel("Description:"));
            descriptionField = new JTextField();
            panel.add(descriptionField);

            panel.add(new JLabel("Priority:"));
            prioritySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
            panel.add(prioritySpinner);

            okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String description = descriptionField.getText().trim();
                    int priority = (int) prioritySpinner.getValue();
                    if (!description.isEmpty()) {
                        task = new Task(description, priority);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(TaskDialog.this, "Description cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            panel.add(okButton);

            cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    task = null;
                    dispose();
                }
            });
            panel.add(cancelButton);

            getContentPane().add(panel);
            pack();
            setLocationRelativeTo(null); // Center the dialog
        }

        public Task getTask() {
            return task;
        }
    }
}
