import openpyxl

# Function to convert a string of numbers into a 2D array
def string_to_2d_array(numbers, rows, cols):
    number_list = [float(num) for num in numbers.split()]
    array2d = []
    for r in range(rows):
        row = []
        for c in range(cols):
            row.append(number_list[r * cols + c])
        array2d.append(row)
    return array2d

# Function to write each row of 2D arrays into consecutive rows in Excel
def write_arrays_to_excel_rows(arrays, output_file):
    # Create a new workbook and select the active worksheet
    workbook = openpyxl.load_workbook(output_file)
    worksheet = workbook.active
    # print(arrays)
    # Write each row of each array into consecutive rows in Excel
    row_idx = 14
    for array2d in arrays:
        coll = 3
        for row in array2d:
            # Write the row of values into the current row in Excel
            for _, value in enumerate(row, start=1):
                worksheet.cell(row=row_idx, column=coll, value=value)
                coll+=1
        row_idx += 1  # Move to the next row for the next row of values

    # Save the workbook
    workbook.save(output_file)
    print(f"Successfully wrote arrays to {output_file}")

# Function to process strings from a text file and convert them to 2D arrays
def process_strings_from_file(input_file):
    # Read strings from text file
    with open(input_file, 'r') as file:
        strings = file.readlines()

    arrays = []
    sizeList = [(3,4),(3,4),(3,4),(4,12),(6,12),(5,5),(5,5),(7,7),(7,7),(7,7)]
    i=0
    for string in strings:
        array2d = string_to_2d_array(string.strip(), sizeList[int(i/3)][0], sizeList[int(i/3)][1])
        arrays.append(array2d)
        i+=1
    return arrays

# Example usage
input_file = 'results_for_xl.txt'  # Path to the input text file containing strings
output_file = '213934599_207302027.xlsx'  # Path to save the Excel file

# Process strings from file and convert them to 2D arrays
arrays = process_strings_from_file(input_file)

# Write each row of arrays row by row into Excel
write_arrays_to_excel_rows(arrays, output_file)
