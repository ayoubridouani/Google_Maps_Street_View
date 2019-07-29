-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Mar 16, 2019 at 05:07 PM
-- Server version: 10.3.13-MariaDB
-- PHP Version: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id8362570_monuments`
--

-- --------------------------------------------------------

--
-- Table structure for table `monuments`
--

CREATE TABLE `monuments` (
  `id` int(6) NOT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `image1` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `image2` varchar(255) CHARACTER SET utf8 NOT NULL,
  `about` text COLLATE utf8_unicode_ci NOT NULL,
  `description` text COLLATE utf8_unicode_ci NOT NULL,
  `map` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `monuments`
--

INSERT INTO `monuments` (`id`, `title`, `image1`, `image2`, `about`, `description`, `map`) VALUES
(1, 'm12345', 'benz.jpg', 'bike.jpg', 'just an about', 'just a  desc', ''),
(2, 'm222', 'bike.jpg', 'carrera.jpg', 'just an about', 'just a  desc', ''),
(3, 'm333', 'carrera.jpg', 'ferrari.jpg', 'just an about', 'just a  desc', ''),
(4, 'm444', 'ferrari.jpg', 'harly.jpg', 'just an about', 'just a  desc', ''),
(5, 'm555', 'harly.jpg', 'benz.jpg', 'just an about', 'just a  desc', '');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(5) UNSIGNED NOT NULL,
  `fname` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `lname` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `user` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `pass` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `fname`, `lname`, `email`, `user`, `pass`) VALUES
(1, 'Ayoub', 'Ridouani', 'ridouaniayoub@gmail.com', 'ayoubridouani', '5F63D3D843A143FC3D9353B40D3D04BA');

-- --------------------------------------------------------

--
-- Table structure for table `villes`
--

CREATE TABLE `villes` (
  `id` int(6) NOT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `image1` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `image2` varchar(255) CHARACTER SET utf8 NOT NULL,
  `about` text COLLATE utf8_unicode_ci NOT NULL,
  `description` text COLLATE utf8_unicode_ci NOT NULL,
  `map` varchar(255) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `villes`
--

INSERT INTO `villes` (`id`, `title`, `image1`, `image2`, `about`, `description`, `map`) VALUES
(1, 'v12345', 'benz.jpg', 'bike.jpg', 'just an about', 'just a  desc', ''),
(2, 'v222', 'bike.jpg', 'carrera.jpg', 'just an about', 'just a  desc', ''),
(3, 'v333', 'carrera.jpg', 'ferrari.jpg', 'just an about', 'just a  desc', ''),
(4, 'v444', 'ferrari.jpg', 'harly.jpg', 'just an about', 'just a  desc', ''),
(5, 'v555', 'harly.jpg', 'benz.jpg', 'just an about', 'just a  desc', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `monuments`
--
ALTER TABLE `monuments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user` (`user`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `villes`
--
ALTER TABLE `villes`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `monuments`
--
ALTER TABLE `monuments`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `villes`
--
ALTER TABLE `villes`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
