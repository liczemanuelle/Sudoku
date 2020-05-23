rmdir /Q/S build
mkdir build
javac -d build src/edu/sapi/mestint/*.java
cd build
jar cvfe ../SudokuSolver.jar edu.sapi.mestint.Sudoku edu/sapi/mestint/*.class
cd ..