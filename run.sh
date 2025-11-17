#!/bin/bash
echo "Compiling Neon Shatter..."
javac -source 8 -target 8 -d bin -sourcepath src src/*.java
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi
echo ""
echo "Starting game..."
echo ""
java -cp bin NeonShatter

