
<?php
// Establish MySQL connection
$servername = "localhost";
$username = "root";
$password = "Aar@123";
$database = "TYA";

// Create connection
$conn = new mysqli($servername, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Handle form submission
if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $name = $_POST['name'];
    $mobile = $_POST['number'];
    $department = $_POST['department'];
    $email = $_POST['email'];
    $address = $_POST['address'];
    $action = $_POST['action'];

    if ($action == "add") {
        $sql = "INSERT INTO student (name, number, department, address, email) 
                VALUES ('$name', '$mobile', '$department', '$address', '$email')";
        if ($conn->query($sql) === TRUE) {
            echo "<script>
                    alert('Record inserted successfully');
                    window.location.href='demo.html';
                  </script>";
            exit(); // Ensure no extra output is sent
        } else {
            echo "Error: " . $conn->error;
        }
    } elseif ($action == "delete") {
        $sql = "DELETE FROM student WHERE email='$email'";
        if ($conn->query($sql) === TRUE) {
            echo "<script>
                    alert('Record deleted successfully');
                    window.location.href='demo.html';
                  </script>";
            exit();
        } else {
            echo "Error: " . $conn->error;
        }
    } elseif ($action == "update") {
        $sql = "UPDATE student SET name='$name', number='$mobile', department='$department', address='$address' WHERE email='$email'";
        if ($conn->query($sql) === TRUE) {
            echo "<script>
                    alert('Record updated successfully');
                    window.location.href='demo.html';
                  </script>";
            exit();
        } else {
            echo "Error: " . $conn->error;
        }
    } elseif ($action == "display") {
        $sql = "SELECT * FROM student";
        $result = $conn->query($sql);
        if ($result->num_rows > 0) {
            echo "<table border='1'><tr><th>Name</th><th>Number</th><th>Department</th><th>Address</th><th>Email</th></tr>";
            while ($row = $result->fetch_assoc()) {
                echo "<tr><td>{$row['name']}</td><td>{$row['number']}</td><td>{$row['department']}</td><td>{$row['address']}</td><td>{$row['email']}</td></tr>";
            }
            echo "</table>";
        } else {
            echo "No records found.";
        }
    }
}

// Close connection
$conn->close();
?>
