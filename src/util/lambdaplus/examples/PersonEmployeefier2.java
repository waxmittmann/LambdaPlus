package util.lambdaplus.examples;

import util.lambdaplus.lambda.experimental.GuardedFunction2;
import util.lambdaplus.lambda.util.general.Tuple2;

import java.util.Optional;
import java.util.function.Function;

import static util.lambdaplus.lambda.util.general.Tuple2.extractSecond;

/*
public class PersonEmployeefier2 {

    public static class Employee {
        int salary;
        EducationLevel educationLevel;
        ExperienceLevel experienceLevel;
        String name;
        int age;

        public int getSalary() {
            return salary;
        }

        public void setSalary(int salary) {
            this.salary = salary;
        }

        public EducationLevel getEducationLevel() {
            return educationLevel;
        }

        public void setEducationLevel(EducationLevel educationLevel) {
            this.educationLevel = educationLevel;
        }

        public ExperienceLevel getExperienceLevel() {
            return experienceLevel;
        }

        public void setExperienceLevel(ExperienceLevel experienceLevel) {
            this.experienceLevel = experienceLevel;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Employee() {
        }

//        public boolean isEmployable() {
//            return false;
//        }
    }

    public static enum EducationLevel {
        NONE(0), SCHOOL(1), HIGH_SCHOOL(2), UNIVERSITY(3), DOCTORATE(4);
        private final int level;

        EducationLevel(int level) {
            this.level = level;
        }

        public Boolean atLeast(EducationLevel otherLevel) {
            return level >= otherLevel.level;
        }
    }

    public static enum ExperienceLevel {
        NONE(0), _1_2_YEARS(1), _3_5_YEARS(2), _6_PLUS_YEARS(3);
        private final int level;

        ExperienceLevel(int level) {
            this.level = level;
        }

        public Boolean atLeast(ExperienceLevel otherLevel) {
            return level >= otherLevel.level;
        }
    }

    public static class Person {
        EducationLevel educationLevel;
        ExperienceLevel experienceLevel;
        String name;
        int age;

        public EducationLevel getEducationLevel() {
            return educationLevel;
        }

        public ExperienceLevel getExperienceLevel() {
            return experienceLevel;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public Person(EducationLevel educationLevel,
                      ExperienceLevel experienceLevel, String name, int age) {
            this.educationLevel = educationLevel;
            this.experienceLevel = experienceLevel;

            this.name = name;
            this.age = age;
        }
    }

    public static void main (String [] args) {
        GuardedFunction2<Person, Tuple2<Person, Employee>> guardedFunc2 = GuardedFunction2.fromFunc(
            (Person person) -> new Tuple2<>(person, transferEducation(person, new Employee())),
            (Person __) -> true,
            (Tuple2<Person, Employee> in) -> in.getFirst().getEducationLevel().atLeast(EducationLevel.HIGH_SCHOOL)
        );

        Optional<Tuple2<Person, Employee>> op = guardedFunc2.apply(null);

        final Function<Tuple2<Person, Employee>, Tuple2<Person, Employee>> function = (Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(),
                transferExperience(in.getFirst(), in.getSecond()));
        final Function<Person, Boolean> alwaysTrue = (Person __) -> true;
//        final Function<Tuple2<Person, Employee>, Boolean> isEmployable = (Tuple2<Person, Employee> in) -> in.getSecond().getEducationLevel().atLeast(EducationLevel.HIGH_SCHOOL);


//        GuardedFunction2<Person, Tuple2<Person, Employee>> guardedFunc3 =
//            GuardedFunction2.<Person, Tuple2<Person, Employee>>fromFunc(
//                (Person person) -> new Tuple2<Person, Employee>(person, transferEducation(person, new Employee())),
//                (Person __) -> true,
//                (Tuple2<Person, Employee> in) -> in.getSecond().getEducationLevel().atLeast(EducationLevel.HIGH_SCHOOL)
//            )
//            .andThen2(function,
//                    alwaysTrue,
//                    (Tuple2<Person, Employee> in) -> in.getSecond().getExperienceLevel().atLeast(ExperienceLevel._1_2_YEARS));

//        GuardedFunction2<Person, Tuple2<Person, Employee>> guardedFunc3b =
//                GuardedFunction2.<Person, Tuple2<Person, Employee>>fromFunc(
//                        (Person person) -> new Tuple2<Person, Employee>(person, transferEducation(person, new Employee())),
//                        (Person __) -> true,
//                        (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable()
//                )
//                        .<Tuple2<Person, Employee>>andThen2((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(),
//                                transferExperience(in.getFirst(), in.getSecond())),
//                                alwaysTrue,
//                                isEmployable);

        GuardedFunction2<Person, Tuple2<Person, Employee>> guardedFunc3b =
                guardedFunc2
                .andThen2((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(),
                    transferExperience(in.getFirst(), in.getSecond())), alwaysTrue,
                    (Tuple2 < Person, Employee > in) -> in.getFirst().getExperienceLevel().atLeast(ExperienceLevel._1_2_YEARS)
                )
                .andThen2((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(),
                    transferEducation(in.getFirst(), in.getSecond())), alwaysTrue,
                    (Tuple2 < Person, Employee > in) -> in.getFirst().getEducationLevel().atLeast(
                            EducationLevel.HIGH_SCHOOL)
                );

        Person somePerson = new Person(EducationLevel.DOCTORATE, ExperienceLevel._1_2_YEARS, "Max Wittmann", 32);
//        Optional<Tuple2<Person, Employee>> personEmployee = guardedFunc3b.apply(somePerson);
        Optional<Employee> employee = extractSecond(guardedFunc3b.apply(somePerson));
        employee.ifPresent((Employee employee2) -> System.out.println("Hired: " + employee2));


//            .andThen2((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(), transferAge(in.getFirst(), in.getSecond())),
//                    (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable());

//            .andThen2(((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(),
//                    transferName(in.getFirst(), in.getSecond())),
//                    (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable())
//            .getSecond();



//                .andThen2(((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(), transferAge(in.getFirst(), in.getSecond())),
//                        (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable())
//                .andThen2(((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(),
//                        transferName(in.getFirst(), in.getSecond())),
//                        (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable())
//                .getSecond();


//        Employee newEmployee =
//            new GuardedFunction2<Person, Tuple2<Person, Employee>>(
//                    (Person person) -> new Tuple2(person, transferEducation(person, new Employee())),
//                    (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable())
//                .andThen2((Tuple2<Person, Employee> in)-> new Tuple2(in.getFirst(), transferExperience(in.getFirst(), in.getSecond())),
//                        (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable())
//                .andThen2(((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(), transferAge(in.getFirst(), in.getSecond())),
//                        (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable())
//                .andThen2(((Tuple2<Person, Employee> in) -> new Tuple2(in.getFirst(),
//                        transferName(in.getFirst(), in.getSecond())),
//                        (Tuple2<Person, Employee> in) -> in.getSecond().isEmployable())
//                .getSecond();

    }

    private static Employee transferExperience(Person person, Employee employee) {
        return null;
    }

    private static Employee transferName(Person person, Employee employee) {
        return null;
    }

    private static Employee transferAge(Person person, Employee employee) {
        return null;
    }

    private static Employee transferEducation(Person person, Employee employee) {
        return null;
    }
}
*/