@del /S *.class
@"%javac%" MainApp.java
@"%java%" MainApp
@del /S *.class