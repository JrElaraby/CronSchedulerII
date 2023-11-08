#!/bin/bash

directory_name="my_new_directory"

if [ -d "$directory_name" ]; then
    echo "Directory '$directory_name' already exists."
    exit 1
fi

mkdir "$directory_name"

if [ $? -eq 0 ]; then
    echo "Directory '$directory_name' created successfully."
else
    echo "Failed to create directory '$directory_name'."
    exit 1
fi