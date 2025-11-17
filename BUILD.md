# Building and Running Neon Shatter

## Quick Start

### Windows
Double-click `run.bat` or run:
```cmd
run.bat
```

### Linux/Mac
```bash
chmod +x run.sh
./run.sh
```

## Manual Build

### Compile
```bash
javac -source 8 -target 8 -d bin -sourcepath src src/*.java
```

### Run
```bash
java -cp bin NeonShatter
```

## Troubleshooting

### Window Not Opening
1. Make sure Java is installed: `java -version`
2. Check if another instance is running
3. Try running from command line to see error messages

### Compilation Errors
- Ensure JDK 8+ is installed
- Check that all source files are in the `src/` directory
- Delete `bin/` folder and recompile

### Runtime Errors
- Verify Java version: `java -version` (should be 8+)
- Check classpath: `java -cp bin NeonShatter`
- Ensure all `.class` files are in `bin/` directory

