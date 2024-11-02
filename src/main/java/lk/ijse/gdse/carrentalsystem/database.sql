-- Create the database
CREATE DATABASE CarRentalSystem;

-- Use the newly created database
USE CarRentalSystem;

-- Create the Admin table
CREATE TABLE Admin (
                       admin_id VARCHAR(50) NOT NULL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

-- Create the Package table
CREATE TABLE Package (
                         package_id VARCHAR(50) NOT NULL PRIMARY KEY,
                         package_name VARCHAR(100) NOT NULL,
                         total_cost DECIMAL(10, 2) NOT NULL,
                         insurance_included BOOLEAN NOT NULL,
                         rental_duration VARCHAR(50) NOT NULL,
                         rent_date DATE NOT NULL,
                         mileage_limit VARCHAR(50) NOT NULL,
                         description VARCHAR(255)
);

-- Create the Employee table
CREATE TABLE Employee (
                          emp_id VARCHAR(50) NOT NULL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          address VARCHAR(255),
                          job_role VARCHAR(50) NOT NULL,
                          salary DECIMAL(10, 2) NOT NULL,
                          admin_id VARCHAR(50),
                          FOREIGN KEY (admin_id) REFERENCES Admin(admin_id)
                              ON UPDATE CASCADE
                              ON DELETE CASCADE
);

-- Create the Customer table
CREATE TABLE Customer (
                          cust_id VARCHAR(50) NOT NULL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          address VARCHAR(255),
                          email VARCHAR(100) NOT NULL,
                          nic_number VARCHAR(20) NOT NULL UNIQUE,
                          admin_id VARCHAR(50),
                          FOREIGN KEY (admin_id) REFERENCES Admin(admin_id)
                              ON UPDATE CASCADE
                              ON DELETE CASCADE
);

-- Create the Rental_Agreement table
CREATE TABLE Rental_Agreement (
                                  agreement_id VARCHAR(50) NOT NULL PRIMARY KEY,
                                  payment_terms VARCHAR(255) NOT NULL,
                                  start_date DATE NOT NULL,
                                  end_date DATE NOT NULL,
                                  deposit_amount DECIMAL(10, 2) NOT NULL,
                                  total_rent_cost DECIMAL(10, 2) NOT NULL
);

-- Create the Rent table
CREATE TABLE Rent (
                      rent_id VARCHAR(50) NOT NULL PRIMARY KEY,
                      start_date DATE NOT NULL,
                      end_date DATE NOT NULL,
                      cust_id VARCHAR(50) NOT NULL,
                      agreement_id VARCHAR(50) NOT NULL UNIQUE,
                      FOREIGN KEY (cust_id) REFERENCES Customer(cust_id)
                          ON UPDATE CASCADE
                          ON DELETE CASCADE,
                      FOREIGN KEY (agreement_id) REFERENCES Rental_Agreement(agreement_id)
                          ON UPDATE CASCADE
                          ON DELETE CASCADE
);

-- Create the Vehicle table
CREATE TABLE Vehicle (
                         vehicle_id VARCHAR(50) NOT NULL PRIMARY KEY,
                         model VARCHAR(100) NOT NULL,
                         colour VARCHAR(50),
                         category VARCHAR(50) NOT NULL,
                         quantity INT NOT NULL,
                         package_id VARCHAR(50),
                         FOREIGN KEY (package_id) REFERENCES Package(package_id)
                             ON UPDATE CASCADE
                             ON DELETE CASCADE
);

-- Create the Damage table
CREATE TABLE Damage (
                        damage_id VARCHAR(50) NOT NULL PRIMARY KEY,
                        repair_cost DECIMAL(10, 2) NOT NULL,
                        detail VARCHAR(255),
                        rent_id VARCHAR(50) NOT NULL,
                        FOREIGN KEY (rent_id) REFERENCES Rent(rent_id)
                            ON UPDATE CASCADE
                            ON DELETE CASCADE
);

-- Create the Payment table
CREATE TABLE Payment (
                         pay_id VARCHAR(50) NOT NULL PRIMARY KEY,
                         amount DECIMAL(10, 2) NOT NULL,
                         date DATE NOT NULL,
                         invoice VARCHAR(100) UNIQUE,
                         method VARCHAR(50) NOT NULL,
                         transaction_reference VARCHAR(100),
                         tax DECIMAL(10, 2),
                         Discount DECIMAL(10, 2)
);

-- Create the Rent_Payment_Details table
CREATE TABLE Rent_Payment_Details (
                                      rent_id VARCHAR(50) NOT NULL,
                                      pay_id VARCHAR(50) NOT NULL,
                                      payment_date DATE NOT NULL,
                                      duration VARCHAR(20) NOT NULL,
                                      description VARCHAR(255),
                                      pay_amount DECIMAL(10, 2) NOT NULL,
                                      payment_method VARCHAR(50) NOT NULL,
                                      PRIMARY KEY (rent_id, pay_id),
                                      FOREIGN KEY (rent_id) REFERENCES Rent(rent_id)
                                          ON UPDATE CASCADE
                                          ON DELETE CASCADE,
                                      FOREIGN KEY (pay_id) REFERENCES Payment(pay_id)
                                          ON UPDATE CASCADE
                                          ON DELETE CASCADE
);

-- Create the Vehicle_Rent_Details table
CREATE TABLE Vehicle_Rent_Details (
                                      vehicle_id VARCHAR(50) NOT NULL,
                                      rent_id VARCHAR(50) NOT NULL,
                                      start_date DATE NOT NULL,
                                      end_date DATE NOT NULL,
                                      rent_date DATE NOT NULL,
                                      cost DECIMAL(10, 2) NOT NULL,
                                      vehicle_condition VARCHAR(255),
                                      PRIMARY KEY (vehicle_id, rent_id),
                                      FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id)
                                          ON UPDATE CASCADE
                                          ON DELETE CASCADE,
                                      FOREIGN KEY (rent_id) REFERENCES Rent(rent_id)
                                          ON UPDATE CASCADE
                                          ON DELETE CASCADE
);

-- Create the Maintenance table
CREATE TABLE Maintenance (
                             maintain_id VARCHAR(50) NOT NULL PRIMARY KEY,
                             cost DECIMAL(10, 2) NOT NULL,
                             maintain_date DATE NOT NULL,
                             description VARCHAR(255),
                             duration VARCHAR(50) NOT NULL,
                             vehicle_id VARCHAR(50) NOT NULL,
                             FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id)
                                 ON UPDATE CASCADE
                                 ON DELETE CASCADE
);
