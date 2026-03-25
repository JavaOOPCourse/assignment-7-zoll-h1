import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentRecordProcessor {
    // Поля для хранения данных
    private final List<Student> students = new ArrayList<>();

    // _____реализуйте класс Student ниже в этом же файле______

    private double averageScore;
    private Student highestStudent;


    /**
     * Task 1 + Task 2 + Task 5 + Task 6
     */
    public void readFile() {
        // TODO: реализуйте чтение файла здесь
        try (BufferedReader br = new BufferedReader(new FileReader("input/students.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length != 2) {
                    System.out.println("Invalid data" + line);
                    continue;
                }
                String name = parts[0].trim();
                String scoreS = parts[1].trim();
            int score;
            try{
                score = Integer.parseInt(scoreS);
            }catch(NumberFormatException e) {
                System.out.println("Invalid data" + e.getMessage() + line);
                continue;
            }
            try{
                InvalidScoreException.validate(score);
            }catch(InvalidScoreException e) {
                System.out.println("Invalid score " + score + "/" + e.getMessage());
                continue;
            }
            students.add(new Student(name,score));
                System.out.println("Added: " + name + " | " + score);
            }
        }catch(FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    /**
     * Task 3 + Task 8
     */
    public void processData() {
        // TODO: обработка данных и сортировка здесь
        if(students.isEmpty()) {
            System.out.println("No students");
            return;
        }
        int sum = 0;
        for(Student s : students) {
            sum += s.getScore();
        }
        averageScore = (double) sum / students.size();

        highestStudent = students.get(0);
        for(Student s : students) {
            if(s.getScore() > highestStudent.getScore()) {
               highestStudent = s;
            }
        }
        students.sort(new Comparator<Student>() {
            @Override
            public int compare(Student a, Student b) {
                return Integer.compare(b.getScore(),a.getScore());
            }
        });
        System.out.printf("Average: %.1f\n" , averageScore);
        System.out.println("Highest: " + highestStudent.getName()  + " , " + highestStudent.getScore());
    }

    /**
     * Task 4 + Task 5 + Task 8
     */
    public void writeFile() {
        // TODO: запись результата в файл здесь
        new File("output").mkdirs();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("output/report.txt"))) {
            bw.write(String.format("Average %.1f" , averageScore));
            bw.newLine();
            bw.write("Highest: " + highestStudent.getName() + "-" + highestStudent.getScore());
            bw.newLine();

            bw.write("------- Sorted -------");
            bw.newLine();
            for(Student s : students) {
                bw.write(s.getName() + "-" + s.getScore());
                bw.newLine();
            }
        }catch(IOException e) {
            System.out.println("Error while writing : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        StudentRecordProcessor processor = new StudentRecordProcessor();

        try {
            processor.readFile();
            processor.processData();
            processor.writeFile();
            System.out.println("Processing completed. Check output/report.txt");
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}

// class InvalidScoreException реализуйте меня
// class Student (name, score)
class Student {
    private String name;
    private int score;

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
    @Override
    public String toString() {
        return name + "-" + score;
    }
}
class InvalidScoreException extends Exception {

    public InvalidScoreException(int score) {
        super(score + "is out of range(0-100)");
    }
    public static void validate(int score) throws InvalidScoreException {
        if(score < 0 || score > 100)
            throw new InvalidScoreException(score);
    }

}