-- phpMyAdmin SQL Dump
-- version 4.0.10.14
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Mar 25, 2017 at 08:49 AM
-- Server version: 5.6.33-cll-lve
-- PHP Version: 5.6.20

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `bconnect`
--

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE IF NOT EXISTS `comments` (
  `c_id`      INT(11) NOT NULL AUTO_INCREMENT,
  `user_id`   INT(11) NOT NULL,
  `post_id`   INT(11) NOT NULL,
  `timestamp` TEXT    NOT NULL,
  `comment`   TEXT    NOT NULL,
  `published` INT(1)  NOT NULL,
  PRIMARY KEY (`c_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 14;

--
-- Dumping data for table `comments`
--

INSERT INTO `comments` (`c_id`, `user_id`, `post_id`, `timestamp`, `comment`, `published`) VALUES
  (1, 2, 66, '1485900400', 'cool', 1),
  (5, 2, 66, '1486378142', 'Testing Comment', 0),
  (6, 2, 66, '1486378247', 'Testing Comments', 1),
  (7, 2, 45, '1486378331', 'They are Lovely ðŸ˜', 1),
  (8, 1, 66, '1486380062', 'Cool', 0),
  (9, 1, 66, '1486390453', 'Cool ðŸ‘', 1),
  (10, 16, 72, '1486405695', 'empty status bhi jaa raha hai. usko band kar de', 1),
  (11, 2, 74, '1486406021', 'abhi validation nhi dali', 1),
  (12, 1, 45, '1486480353', 'yeah ', 1),
  (13, 1, 94, '1486835022', 'yo bro', 1);

-- --------------------------------------------------------

--
-- Table structure for table `deletepost`
--

CREATE TABLE IF NOT EXISTS `deletepost` (
  `post_id`        INT(11) NOT NULL AUTO_INCREMENT,
  `user_id`        INT(11) NOT NULL,
  `image`          TEXT,
  `status`         TEXT,
  `likes_count`    INT(4)  NOT NULL DEFAULT '0',
  `comments_count` INT(4)  NOT NULL DEFAULT '0',
  `timestamp`      TEXT    NOT NULL,
  `url`            TEXT,
  PRIMARY KEY (`post_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 93;

--
-- Dumping data for table `deletepost`
--

INSERT INTO `deletepost` (`post_id`, `user_id`, `image`, `status`, `likes_count`, `comments_count`, `timestamp`, `url`)
VALUES
  (24, 2, NULL, 'Hello i am bot http://zuts.in', 0, 0, '1483464728', 'http://zuts.in'),
  (46, 2, NULL, 'Alpha Version 0.8 Released ðŸ‘', 0, 0, '1483984605', NULL),
  (61, 2, NULL, 'Public Beta Version of App Released ðŸ’Ÿ', 0, 0, '1484485170', NULL),
  (91, 2, NULL, '\n', 0, 0, '1486577156', NULL),
  (92, 2, 'http://codejuris.com/BITConnect/TEst/./images/uploads/rgjhd5tsdy46hm8t55h5.jpg', '', 0, 0, '1486577346',
   NULL);

-- --------------------------------------------------------

--
-- Table structure for table `feedback`
--

CREATE TABLE IF NOT EXISTS `feedback` (
  `feedback_id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id`     INT(11) NOT NULL,
  `feedback`    TEXT    NOT NULL,
  `timestamp`   TEXT    NOT NULL,
  PRIMARY KEY (`feedback_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 22;

--
-- Dumping data for table `feedback`
--

INSERT INTO `feedback` (`feedback_id`, `user_id`, `feedback`, `timestamp`) VALUES
  (20, 10, 'oh yeah.. mazaa aavi gyoo', '1484486855'),
  (21, 1, 'Testing ', '1485250063');

-- --------------------------------------------------------

--
-- Table structure for table `likes`
--

CREATE TABLE IF NOT EXISTS `likes` (
  `l_id`        INT(11) NOT NULL AUTO_INCREMENT,
  `user_id`     INT(11) NOT NULL,
  `post_id`     INT(11) NOT NULL,
  `likeboolean` INT(1)  NOT NULL DEFAULT '0',
  `timestamp`   TEXT    NOT NULL,
  PRIMARY KEY (`l_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 73;

--
-- Dumping data for table `likes`
--

INSERT INTO `likes` (`l_id`, `user_id`, `post_id`, `likeboolean`, `timestamp`) VALUES
  (52, 2, 66, 0, '1486411015'),
  (53, 2, 78, 1, '1486412531'),
  (54, 2, 74, 1, '1486412551'),
  (55, 1, 66, 1, '1486412729'),
  (56, 1, 80, 0, '1486412916'),
  (57, 1, 81, 0, '1486443739'),
  (58, 1, 74, 1, '1486443747'),
  (59, 2, 81, 0, '1486472295'),
  (60, 2, 83, 0, '1486472533'),
  (61, 2, 90, 1, '1486473896'),
  (62, 1, 45, 1, '1486480347'),
  (63, 2, 28, 1, '1486577021'),
  (64, 1, 93, 1, '1486606583'),
  (65, 12, 93, 1, '1486607205'),
  (66, 12, 66, 1, '1486607207'),
  (67, 12, 28, 1, '1486607208'),
  (68, 12, 94, 1, '1486828370'),
  (69, 1, 94, 1, '1486835014'),
  (70, 29, 94, 1, '1487607177'),
  (71, 29, 93, 1, '1487607181'),
  (72, 35, 94, 1, '1489119892');

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

CREATE TABLE IF NOT EXISTS `notifications` (
  `n_id`       INT(11) NOT NULL AUTO_INCREMENT,
  `title`      VARCHAR(20)      DEFAULT NULL,
  `body`       TEXT,
  `userid`     INT(11)          DEFAULT NULL,
  `scope`      VARCHAR(10)      DEFAULT NULL,
  `branch`     VARCHAR(10)      DEFAULT NULL,
  `semester`   VARCHAR(10)      DEFAULT NULL,
  `image`      TEXT,
  `created_at` TEXT,
  PRIMARY KEY (`n_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 2;

--
-- Dumping data for table `notifications`
--

INSERT INTO `notifications` (`n_id`, `title`, `body`, `userid`, `scope`, `branch`, `semester`, `image`, `created_at`)
VALUES
  (1, 'Welcome', 'Welcome to BIT Connect', 2, '1', 'BCA', '6', '', '1485171553');

-- --------------------------------------------------------

--
-- Table structure for table `papers`
--

CREATE TABLE IF NOT EXISTS `papers` (
  `p_id`        INT(11)     NOT NULL AUTO_INCREMENT,
  `s_id`        INT(11)              DEFAULT '0',
  `u_id`        INT(11)     NOT NULL,
  `subjectcode` VARCHAR(10) NOT NULL,
  `year`        TEXT,
  `exam`        TEXT,
  `url`         TEXT        NOT NULL,
  PRIMARY KEY (`p_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 80;

--
-- Dumping data for table `papers`
--

INSERT INTO `papers` (`p_id`, `s_id`, `u_id`, `subjectcode`, `year`, `exam`, `url`) VALUES
  (39, 23, 1, 'BCA2005', '2015', 'Mid Semester',
   'http://codejuris.com/BITConnect/TEst/./images/papers/hr0mces0ql2t7hh5c5zj.jpg'),
  (40, 9, 1, 'BCA5003', '2016', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/ovnqfdjqk6jrb3f99slz.jpg'),
  (42, 6, 1, 'BCA5009', '2016', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/kjd4nyjt5mw8qcl61o22.jpg'),
  (44, 7, 1, 'BCA5007', '2016', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/mxxdqq2q7d7l52vr5d4g.jpg'),
  (45, 8, 1, 'BCA5005', '2016', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/edi2poh5poc7qz0okbxv.jpg'),
  (46, 10, 1, 'BCA4009', '2016', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/9m2fk74lz9m1drfq74qj.jpg'),
  (47, 9, 1, 'BCA5003', '2016', 'End Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/ko0czcrqt1lxwofxq08h.jpg'),
  (48, 8, 1, 'BCA5005', '2016', 'End Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/oh3jfwxpllc1d0iv1ysl.jpg'),
  (49, 7, 1, 'BCA5007', '2016', 'End Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/4lpfzvwjtmogqu0t8x23.jpg'),
  (50, 6, 1, 'BCA5009', '2016', 'End Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/sfh57n97lqlzrveplddw.jpg'),
  (51, 10, 1, 'BCA4009', '2015', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/g0fjlvonxsf0tvs7vgks.jpg'),
  (52, 8, 1, 'BCA5005', '2015', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/grhuhjsk6x21hklscb5b.jpg'),
  (53, 8, 1, 'BCA5005', '2015', 'End Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/6hmleog92cw17qwfze3x.jpg'),
  (54, 6, 1, 'BCA5009', '2015', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/3nu4c3p19phjhvi0vbtt.jpg'),
  (55, 6, 1, 'BCA5009', '2015', 'End Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/l2id63uhmveegzold4ox.jpg'),
  (56, 7, 1, 'BCA5007', '2015', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/j5vz6uu45364npai3rgc.jpg'),
  (57, 7, 1, 'BCA5007', '2015', 'End Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/pcbcomnpp89pl49ajlhx.jpg'),
  (58, 9, 1, 'BCA5003', '2015', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/t8wdw8lgdob6q1qn9bl6.jpg'),
  (59, 9, 1, 'BCA5003', '2015', 'End Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/iv6gplxc1m66w1wm340s.jpg'),
  (60, 5, 1, 'BCA6013', '2016', 'End Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/6mw9x828z7qrl0rgviux.jpg'),
  (61, 4, 1, 'BCA6007', '2016', 'End Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/43x0amyv2nws9rnhf0gx.jpg'),
  (62, 2, 1, 'BCA6003', '2016', 'End Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/6dfp4j5ffic99qi0lmad.jpg'),
  (63, 5, 1, 'BCA6013', '2016', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/9ut4vsb9i69dj1tl5891.jpg'),
  (64, 2, 1, 'BCA6003', '2016', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/d8pargsze10ojorp3fl2.jpg'),
  (65, 3, 1, 'BCA6005', '2016', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/dkqt0hklpsmyhj3st4t1.jpg'),
  (66, 1, 1, 'BCA6001', '2016', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/df4t81bwkt68mcngj689.jpg'),
  (67, 3, 1, 'BCA6005', '2016', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/p25rkgnw0t50ei6elprq.jpg'),
  (68, 1, 1, 'BCA6001', '2016', 'End Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/w23jlr5locuczv49q832.jpg'),
  (69, 1, 1, 'BCA6001', '2016', 'End Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/1woycpm1d0okac8wxs84.jpg'),
  (72, 0, 2, 'BCA6001', '2017', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/o3ms2kxzb3fqyh94rtlh.jpg'),
  (73, 0, 1, 'BCA6007', '2016', 'Mid Semester', 'http://codejuris.com/BITConnect/TEst/./images/papers/21x3xbx119xr558pgj59.jpg'),
  (74, 0, 1, 'BCA6007', '2016', 'Mid Semester',
   'http://codejuris.com/BITConnect/TEst/./images/papers/e8o0tlhogl5d5vdv2am9.jpg'),
  (75, 0, 1, 'BCA6007', '2016', 'Mid Semester',
   'http://codejuris.com/BITConnect/TEst/./images/papers/cnchc3p58kk3c64mp1oz.jpg'),
  (76, 0, 1, 'BCA6007', '2016', 'Mid Semester',
   'http://codejuris.com/BITConnect/TEst/./images/papers/31laigae6swk5auz3y2f.jpg'),
  (77, 0, 1, 'BCA6003', '2017', 'Mid Semester',
   'http://codejuris.com/BITConnect/TEst/./images/papers/0dypks1nrlpz5ilj8806.jpg'),
  (78, 0, 1, 'BCA6001', '2017', 'Mid Semester',
   'http://codejuris.com/BITConnect/TEst/./images/papers/9dz33zmtl1izcdxwbzij.jpg'),
  (79, 0, 1, 'BCA6001', '2017', 'Mid Semester',
   'http://codejuris.com/BITConnect/TEst/./images/papers/yyc3oeipxh466owtoe7a.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `posts`
--

CREATE TABLE IF NOT EXISTS `posts` (
  `post_id`        INT(11) NOT NULL AUTO_INCREMENT,
  `user_id`        INT(11) NOT NULL,
  `image`          LONGTEXT,
  `status`         TEXT,
  `likes_count`    INT(4) UNSIGNED  DEFAULT '0',
  `comments_count` INT(4) UNSIGNED  DEFAULT '0',
  `timestamp`      TEXT    NOT NULL,
  `url`            TEXT,
  PRIMARY KEY (`post_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 95;

--
-- Dumping data for table `posts`
--

INSERT INTO `posts` (`post_id`, `user_id`, `image`, `status`, `likes_count`, `comments_count`, `timestamp`, `url`)
VALUES
  (28, 1, NULL,
   'BIT Connect is on Play Store in beta version.\n\n\nhttps://play.google.com/store/apps/details?id=zuts.bit.connect',
   2, 0, '1483476811', 'https://play.google.com/store/apps/details?id=zuts.bit.connect'),
  (66, 2, NULL, 'Now the app supports Like and Comments also.. ðŸ˜Š', 2, 3, '1485954269', NULL),
  (74, 16, NULL, '', 2, 1, '1486405658', NULL),
  (93, 1, 'http://codejuris.com/BITConnect/TEst/./images/uploads/qt5bcckeyhlb48v8lbh4.jpg',
   'Stable Beta version Released âœŒ\nhttps://play.google.com/store/apps/details?id=zuts.bit.connect', 3, 0,
   '1486579431', 'https://play.google.com/store/apps/details?id=zuts.bit.connect'),
  (94, 22, NULL, 'nice app', 4, 1, '1486649580', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `report`
--

CREATE TABLE IF NOT EXISTS `report` (
  `r_id`   INT(11) NOT NULL AUTO_INCREMENT,
  `imgurl` TEXT,
  `uname`  TEXT,
  `status` TEXT,
  `uid`    INT(11) NOT NULL,
  `report` TEXT,
  PRIMARY KEY (`r_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `resources`
--

CREATE TABLE IF NOT EXISTS `resources` (
  `r_id` INT(11) NOT NULL AUTO_INCREMENT,
  `s_id` INT(11) NOT NULL,
  `url`  TEXT    NOT NULL,
  PRIMARY KEY (`r_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `subject`
--

CREATE TABLE IF NOT EXISTS `subject` (
  `s_id`        INT(11)     NOT NULL AUTO_INCREMENT,
  `subjectcode` TEXT,
  `subjectname` TEXT,
  `branch`      VARCHAR(10) NOT NULL,
  `semester`    VARCHAR(10) NOT NULL,
  PRIMARY KEY (`s_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 102;

--
-- Dumping data for table `subject`
--

INSERT INTO `subject` (`s_id`, `subjectcode`, `subjectname`, `branch`, `semester`) VALUES
  (1, 'BCA6001', 'Operations Research', 'BCA', '6'),
  (2, 'BCA6003', 'Fundamentals of Computer Algorithms', 'BCA', '6'),
  (3, 'BCA6005', 'Computer Network', 'BCA', '6'),
  (4, 'BCA6007', 'Data Mining And WareHousing', 'BCA', '6'),
  (5, 'BCA6013', 'System Programming', 'BCA', '6'),
  (6, 'BCA5009', 'Introduction to Accounting', 'BCA', '5'),
  (7, 'BCA5007', 'Internet and Web Technology', 'BCA', '5'),
  (8, 'BCA5005', 'VB.NET Programming', 'BCA', '5'),
  (9, 'BCA5003', 'Data Communication', 'BCA', '5'),
  (10, 'BCA5001', 'Probability and Statistics', 'BCA', '5'),
  (11, 'BCA4009', 'Computer Graphics and Multimedia', 'BCA', '4'),
  (12, 'BCA4007', 'Computer Architecture', 'BCA', '4'),
  (13, 'BCA4005', 'Software Engineering Principles', 'BCA', '4'),
  (14, 'BCA4003', 'Fundamentals of Management Information System', 'BCA', '4'),
  (15, 'BCA4001', 'Scientific Computing', 'BCA', '4'),
  (16, 'BCA3009', 'Database Management System', 'BCA', '3'),
  (17, 'BCA3007', 'Programming in JAva', 'BCA', '3'),
  (18, 'BCA3005', 'Electronic Commerce and Applications', 'BCA', '3'),
  (19, 'BCA3003', 'Operating System', 'BCA', '3'),
  (20, 'BCA3001', 'Discrete Mathematics Structures', 'BCA', '3'),
  (21, 'BCA2009', 'Managerial Economics', 'BCA', '2'),
  (22, 'BCA2007', 'Logic Desing', 'BCA', '2'),
  (23, 'BCA2005', 'Object Oriented Programming in C++', 'BCA', '2'),
  (24, 'BCA2003', 'Data Structures', 'BCA', '2'),
  (25, 'BCA2001', 'Mathematics-II', 'BCA', '2'),
  (26, 'BCA1009', 'Communication Skills / Technical English', 'BCA', '1'),
  (27, 'BCA1007', 'Environmental Science', 'BCA', '1'),
  (28, 'BCA1005', 'Procramming in C', 'BCA', '1'),
  (29, 'BCA1003', 'Introduction to Computer Science', 'BCA', '1'),
  (30, 'BCA1001', 'Mathematics-I', 'BCA', '1'),
  (86, 'CS3005', 'Object Oriented Programming using Java', 'BE-CSE', '3'),
  (87, 'CS4101', 'Discrete Mathematical Structures', 'BE-CSE', '3'),
  (88, 'CS6101', 'Design and Analysis of Computer Algorithms', 'BE-CSE', '3'),
  (89, 'CS3201', 'Digital Electronics', 'BE-CSE', '3'),
  (90, 'CS3006', 'Object Oriented Programming Lab', 'BE-CSE', '3'),
  (91, 'BT3021', 'Biological Science', 'BE-ECE', '3'),
  (92, 'EC3201', 'Digital Electronics', 'BE-ECE', '3'),
  (93, 'EC3203', 'Modern Instruments and Measurement', 'BE-ECE', '3'),
  (94, 'EC3205', 'Semiconductor Devices', 'BE-ECE', '3'),
  (95, 'EC3205', 'Network Theory', 'BE-ECE', '3'),
  (96, 'BT3021', 'Biological Science', 'BE-EEE', '3'),
  (97, 'EC3201', 'Digital Electronics', 'BE-EEE', '3'),
  (98, 'EE3201', 'Introduction to System Theory', 'BE-EEE', '3'),
  (99, 'EE3205', 'Network Theory', 'BE-EEE', '3'),
  (100, 'EE3207', 'Electric Energy Generation and Control', 'BE-EEE', '3'),
  (101, 'EE3212', 'Computing Lab', 'BE-EEE', '3');

-- --------------------------------------------------------

--
-- Table structure for table `subjectclasslist`
--

CREATE TABLE IF NOT EXISTS `subjectclasslist` (
  `id`          INT(11)     NOT NULL AUTO_INCREMENT,
  `subjectcode` TEXT,
  `branch`      VARCHAR(10) NOT NULL,
  `semester`    VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 288;

--
-- Dumping data for table `subjectclasslist`
--

INSERT INTO `subjectclasslist` (`id`, `subjectcode`, `branch`, `semester`) VALUES
  (102, 'BCA1001', 'BCA', '1'),
  (103, 'BCA1003', 'BCA', '1'),
  (104, 'BCA1005', 'BCA', '1'),
  (105, 'BCA1007', 'BCA', '1'),
  (106, 'BCA1009', 'BCA', '1'),
  (107, 'BCA2001', 'BCA', '2'),
  (108, 'BCA2003', 'BCA', '2'),
  (109, 'BCA2005', 'BCA', '2'),
  (110, 'BCA2007', 'BCA', '2'),
  (111, 'BCA2009', 'BCA', '2'),
  (112, 'BCA3001', 'BCA', '3'),
  (113, 'BCA3003', 'BCA', '3'),
  (114, 'BCA3005', 'BCA', '3'),
  (115, 'BCA3007', 'BCA', '3'),
  (116, 'BCA3009', 'BCA', '3'),
  (117, 'BCA4001', 'BCA', '4'),
  (118, 'BCA4003', 'BCA', '4'),
  (119, 'BCA4005', 'BCA', '4'),
  (120, 'BCA4007', 'BCA', '4'),
  (121, 'BCA4009', 'BCA', '4'),
  (122, 'BCA5001', 'BCA', '5'),
  (123, 'BCA5003', 'BCA', '5'),
  (124, 'BCA5005', 'BCA', '5'),
  (125, 'BCA5007', 'BCA', '5'),
  (126, 'BCA5009', 'BCA', '5'),
  (127, 'BCA6001', 'BCA', '6'),
  (128, 'BCA6003', 'BCA', '6'),
  (129, 'BCA6005', 'BCA', '6'),
  (130, 'BCA6007', 'BCA', '6'),
  (131, 'BCA6013', 'BCA', '6'),
  (162, 'CH1401', 'BE-CSE', '1'),
  (163, 'CS1302', 'BE-CSE', '1'),
  (164, 'EE2201', 'BE-CSE', '1'),
  (165, 'HU1101', 'BE-CSE', '1'),
  (166, 'MA1201', 'BE-CSE', '1'),
  (167, 'PH1201', 'BE-CSE', '1'),
  (168, 'AM1201', 'BE-CSE', '2'),
  (169, 'CH2203', 'BE-CSE', '2'),
  (170, 'CS2301', 'BE-CSE', '2'),
  (171, 'EC2001', 'BE-CSE', '2'),
  (172, 'MA2201', 'BE-CSE', '2'),
  (173, 'ME2001', 'BE-CSE', '2'),
  (174, 'CS2302', 'BE-CSE', '2'),
  (175, 'CP7117', 'BE-CSE', '7'),
  (176, 'CS5105', 'BE-CSE', '7'),
  (177, 'MSH1109', 'BE-CSE', '7'),
  (178, 'CS5106', 'BE-CSE', '7'),
  (179, 'CS7127', 'BE-CSE', '8'),
  (180, 'CS8029', 'BE-CSE', '8'),
  (181, 'CS8031', 'BE-CSE', '8'),
  (182, 'CH1401', 'BE-ECE', '1'),
  (183, 'CS1302', 'BE-ECE', '1'),
  (184, 'EE2201', 'BE-ECE', '1'),
  (185, 'HU1101', 'BE-ECE', '1'),
  (186, 'MA1201', 'BE-ECE', '1'),
  (187, 'PH1201', 'BE-ECE', '1'),
  (188, 'AM1201', 'BE-ECE', '2'),
  (189, 'CH2203', 'BE-ECE', '2'),
  (190, 'CS2301', 'BE-ECE', '2'),
  (191, 'EC2001', 'BE-ECE', '2'),
  (192, 'MA2201', 'BE-ECE', '2'),
  (193, 'ME2001', 'BE-ECE', '2'),
  (194, 'CS2302', 'BE-ECE', '2'),
  (195, 'EC7201', 'BE-ECE', '7'),
  (196, 'EC7203', 'BE-ECE', '7'),
  (197, 'MEC1125', 'BE-ECE', '7'),
  (198, 'MEC2001', 'BE-ECE', '7'),
  (199, 'MSH1109', 'BE-ECE', '7'),
  (200, 'MEC2067', 'BE-ECE', '8'),
  (201, 'CH1401', 'BE-EEE', '1'),
  (202, 'CS1302', 'BE-EEE', '1'),
  (203, 'EE2201', 'BE-EEE', '1'),
  (204, 'HU1101', 'BE-EEE', '1'),
  (205, 'MA1201', 'BE-EEE', '1'),
  (206, 'PH1201', 'BE-EEE', '1'),
  (207, 'AM1201', 'BE-EEE', '2'),
  (208, 'CH2203', 'BE-EEE', '2'),
  (209, 'CS2301', 'BE-EEE', '2'),
  (210, 'EC2001', 'BE-EEE', '2'),
  (211, 'MA2201', 'BE-EEE', '2'),
  (212, 'ME2001', 'BE-EEE', '2'),
  (213, 'CS2302', 'BE-EEE', '2'),
  (214, 'CS4205', 'BE-EEE', '7'),
  (215, 'EE7203', 'BE-EEE', '7'),
  (216, 'MEE1151', 'BE-EEE', '7'),
  (217, 'MSH1109', 'BE-EEE', '7'),
  (218, 'EE8221', 'BE-EEE', '8'),
  (219, 'BT3201', 'BE-CSE', '3'),
  (220, 'CS3005', 'BE-CSE', '3'),
  (221, 'CS4101', 'BE-CSE', '3'),
  (222, 'CS6101', 'BE-CSE', '3'),
  (223, 'EC3201', 'BE-CSE', '3'),
  (224, 'CS3006', 'BE-CSE', '3'),
  (225, 'BT3201', 'BE-ECE', '3'),
  (226, 'EC3203', 'BE-ECE', '3'),
  (227, 'EC3209', 'BE-ECE', '3'),
  (228, 'EC3205', 'BE-ECE', '3'),
  (229, 'BT3201', 'BE-EEE', '3'),
  (230, 'EC3201', 'BE-EEE', '3'),
  (231, 'EE3205', 'BE-EEE', '3'),
  (232, 'EE3207', 'BE-EEE', '3'),
  (233, 'EE3212', 'BE-EEE', '3'),
  (234, 'EE3201', 'BE-EEE', '3'),
  (235, 'CS4107', 'BE-CSE', '4'),
  (236, 'CS4109', 'BE-CSE', '4'),
  (237, 'CS4205', 'BE-CSE', '4'),
  (238, 'HU4001', 'BE-CSE', '4'),
  (239, 'HU4001', 'BE-CSE', '4'),
  (240, 'MA4109', 'BE-CSE', '4'),
  (241, 'CS4108', 'BE-CSE', '4'),
  (242, 'CS4206', 'BE-CSE', '4'),
  (243, 'MA4110', 'BE-ECE', '4'),
  (244, 'EC4201', 'BE-ECE', '4'),
  (245, 'EC4203', 'BE-ECE', '4'),
  (246, 'EC4205', 'BE-ECE', '4'),
  (247, 'EC4207', 'BE-ECE', '4'),
  (248, 'HU4001', 'BE-ECE', '4'),
  (249, 'EE4201', 'BE-EEE', '4'),
  (250, 'EE4203', 'BE-EEE', '4'),
  (251, 'EE4207', 'BE-EEE', '4'),
  (252, 'EE4209', 'BE-EEE', '4'),
  (253, 'HU4001', 'BE-EEE', '4'),
  (254, 'CS5101', 'BE-CSE', '5'),
  (255, 'CS6107', 'BE-CSE', '5'),
  (256, 'CS8101', 'BE-CSE', '5'),
  (257, 'EC4205', 'BE-CSE', '5'),
  (258, 'PE5011', 'BE-CSE', '5'),
  (259, 'CS6108', 'BE-CSE', '5'),
  (260, 'CS8102', 'BE-CSE', '5'),
  (261, 'EC5201', 'BE-ECE', '5'),
  (262, 'EC5203', 'BE-ECE', '5'),
  (263, 'EC5205', 'BE-ECE', '5'),
  (264, 'EE4207', 'BE-ECE', '5'),
  (265, 'MSH1131', 'BE-ECE', '5'),
  (266, 'EE5201', 'BE-EEE', '5'),
  (267, 'EE5203', 'BE-EEE', '5'),
  (268, 'EE5204', 'BE-EEE', '5'),
  (269, 'EE5207', 'BE-EEE', '5'),
  (270, 'MSH1131', 'BE-EEE', '5'),
  (271, 'CS6011', 'BE-CSE', '6'),
  (272, 'CS6103', 'BE-CSE', '6'),
  (273, 'CS6105', 'BE-CSE', '6'),
  (274, 'CS6109', 'BE-CSE', '6'),
  (275, 'MSH1137', 'BE-CSE', '6'),
  (276, 'CS6102', 'BE-CSE', '6'),
  (277, 'CS6106', 'BE-CSE', '6'),
  (278, 'EC6201', 'BE-ECE', '6'),
  (279, 'EC6203', 'BE-ECE', '6'),
  (280, 'EC6205', 'BE-ECE', '6'),
  (281, 'EE6201', 'BE-ECE', '6'),
  (282, 'MSH1137', 'BE-ECE', '6'),
  (283, 'EE6201', 'BE-EEE', '6'),
  (284, 'EE6203', 'BE-EEE', '6'),
  (285, 'EE6205', 'BE-EEE', '6'),
  (286, 'EE6208', 'BE-EEE', '6'),
  (287, 'MSH1137', 'BE-EEE', '6');

-- --------------------------------------------------------

--
-- Table structure for table `subjectlist`
--

CREATE TABLE IF NOT EXISTS `subjectlist` (
  `s_id`        INT(11) NOT NULL AUTO_INCREMENT,
  `subjectcode` VARCHAR(10)      DEFAULT NULL,
  `subjectname` TEXT,
  PRIMARY KEY (`s_id`),
  UNIQUE KEY `subjectcode` (`subjectcode`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 126;

--
-- Dumping data for table `subjectlist`
--

INSERT INTO `subjectlist` (`s_id`, `subjectcode`, `subjectname`) VALUES
  (1, 'BCA6001', 'Operations Research'),
  (2, 'BCA6003', 'Fundamentals of Computer Algorithms'),
  (3, 'BCA6005', 'Computer Network'),
  (4, 'BCA6007', 'Data Mining And WareHousing'),
  (5, 'BCA6013', 'System Programming'),
  (6, 'BCA5009', 'Introduction to Accounting'),
  (7, 'BCA5007', 'Internet and Web Technology'),
  (8, 'BCA5005', 'VB.NET Programming'),
  (9, 'BCA5003', 'Data Communication'),
  (10, 'BCA5001', 'Probability and Statistics'),
  (11, 'BCA4009', 'Computer Graphics and Multimedia'),
  (12, 'BCA4007', 'Computer Architecture'),
  (13, 'BCA4005', 'Software Engineering Principles'),
  (14, 'BCA4003', 'Fundamentals of Management Information System'),
  (15, 'BCA4001', 'Scientific Computing'),
  (16, 'BCA3009', 'Database Management System'),
  (17, 'BCA3007', 'Programming in JAva'),
  (18, 'BCA3005', 'Electronic Commerce and Applications'),
  (19, 'BCA3003', 'Operating System'),
  (20, 'BCA3001', 'Discrete Mathematics Structures'),
  (21, 'BCA2009', 'Managerial Economics'),
  (22, 'BCA2007', 'Logic Desing'),
  (23, 'BCA2005', 'Object Oriented Programming in C++'),
  (24, 'BCA2003', 'Data Structures'),
  (25, 'BCA2001', 'Mathematics-II'),
  (26, 'BCA1009', 'Communication Skills / Technical English'),
  (27, 'BCA1007', 'Environmental Science'),
  (28, 'BCA1005', 'Procramming in C'),
  (29, 'BCA1003', 'Introduction to Computer Science'),
  (30, 'BCA1001', 'Mathematics-I'),
  (31, 'CS3005', 'Object Oriented Programming using Java'),
  (32, 'CS4101', 'Discrete Mathematical Structures'),
  (33, 'CS6101', 'Design and Analysis of Computer Algorithms'),
  (34, 'CS3201', 'Digital Electronics'),
  (35, 'CS3006', 'Object Oriented Programming Lab'),
  (36, 'BT3201', 'Biological Science'),
  (37, 'EC3201', 'Digital Electronics'),
  (38, 'EC3203', 'Modern Instruments and Measurement'),
  (39, 'EC3209', 'Semiconductor Devices'),
  (40, 'EC3205', 'Network Theory'),
  (41, 'EE3201', 'Introduction to System Theory'),
  (43, 'EE3207', 'Electric Energy Generation and Control'),
  (44, 'EE3212', 'Computing Lab'),
  (45, 'CS4107', 'Operating Systems'),
  (46, 'CS4109', 'Computer System Architecture'),
  (47, 'CS4205', 'Database Management System'),
  (48, 'HU4001', 'French'),
  (49, 'MA4109', 'Probability, Statistics and Numerical Techniques'),
  (50, 'CS4108', 'Operating Systems Lab'),
  (51, 'CS4206', 'DBMS Lab'),
  (52, 'MA4110', 'Numerical Techniques Lab'),
  (53, 'EC4201', 'VLSI Design'),
  (54, 'EC4203', 'Analog Communication System'),
  (55, 'EC4205', 'Microprocessor and Microcontroller'),
  (56, 'EC4207', 'Electromagnetic Theory'),
  (57, 'EE4201', 'Electrical Measurement and Instrumentation'),
  (58, 'EE4203', 'Electrical Machines I'),
  (59, 'EE4207', 'Digital Signal Processing'),
  (60, 'EE4209', 'Engineering Electromagnetics'),
  (61, 'CH1401', 'Engineering Chemistry'),
  (62, 'CS1302', 'Fundamental of UNIX and C Programming'),
  (63, 'EE2201', 'Principles of Electrical Engineering'),
  (64, 'HU1101', 'Technical English'),
  (65, 'MA1201', 'Engineering Mathematics'),
  (66, 'PH1201', 'Physics'),
  (67, 'AM1201', 'Engineering Mechanics'),
  (68, 'CH2203', 'Environmental Science'),
  (69, 'CS2301', 'Fundamentals of Data Structures'),
  (70, 'EC2001', 'Principles of Electronics Engineering'),
  (71, 'MA2201', 'Advanced Engineering Mathematics'),
  (72, 'ME2001', 'Principles of Mechanical Engineering'),
  (73, 'CS2302', 'Data Structures Lab'),
  (74, 'CS5101', 'Formal Language and Automata Theory'),
  (75, 'CS6107', 'Computer Networks'),
  (76, 'CS8101', 'Artificial Intelligence and Expert Systems'),
  (77, 'PE5001', 'Project Engineering'),
  (78, 'CS6108', 'Computer Networking Lab'),
  (79, 'CS8102', 'Artificial Intelligence Lab'),
  (80, 'EC5201', 'Digital Communication System'),
  (81, 'EC5203', 'Microwave Engineering'),
  (82, 'EC5205', 'Data Communication'),
  (94, 'MSH1131', 'Principles of Management'),
  (95, 'EE5201', 'Microprocessor and Microcontroller'),
  (96, 'EE5203', 'Electrical Machines II'),
  (97, 'EE5204', 'Power Electronics'),
  (98, 'EE5207', 'Power System I'),
  (99, 'CS6011', 'Computer Graphics and Multimedia'),
  (100, 'CS6103', 'System Programming'),
  (101, 'CS6105', 'Compiler Design'),
  (102, 'CS6109', 'Software Engineering'),
  (103, 'MSH1137', 'Economics'),
  (104, 'CS6102', 'Computer Graphics and Multimedia Lab'),
  (105, 'CS6106', 'Compiler Design Lab'),
  (106, 'EC6201', 'Intelligent Instrumentation'),
  (107, 'EC6203', 'Fiber Optics Communication System'),
  (108, 'EC6205', 'Computer Networking'),
  (109, 'EE6201', 'Control Theory'),
  (110, 'EE6203', 'Power System II'),
  (111, 'EE6205', 'Industrial Drives & Control'),
  (112, 'EE6208', 'Computer Aided Machine Design'),
  (113, 'CP7117', 'Optimization Techniques'),
  (114, 'CS5105', 'Soft Computing'),
  (115, 'MSH1109', 'Entrepreneurship and Small Business Management'),
  (116, 'CS5106', 'Soft Computing Lab'),
  (117, 'EC7201', 'Mobile and Cellular Communication'),
  (118, 'EC7203', 'Antennas and Propagation for Wireless Communication'),
  (119, 'MEC1125', 'Information Theory and Coding'),
  (120, 'MEC2001', 'Digital Image Processing Techniques'),
  (121, 'MEE1151', 'Advanced Power Electronics'),
  (122, 'CS8029', 'Parallel and Distributed Systems'),
  (123, 'CS8031', 'Data Mining and Data Warehousing'),
  (124, 'MEC2067', 'VDHL & Verilog'),
  (125, 'EE8221', 'Utilization of Electrical Power');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id`         INT(11)      NOT NULL AUTO_INCREMENT,
  `name`       VARCHAR(128) NOT NULL,
  `email`      VARCHAR(128) NOT NULL,
  `branch`     VARCHAR(20)  NOT NULL,
  `semester`   VARCHAR(128) NOT NULL,
  `imgurl`     TEXT,
  `fcm_id`     TEXT         NOT NULL,
  `created_at` TEXT         NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 37;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `email`, `branch`, `semester`, `imgurl`, `fcm_id`, `created_at`) VALUES
  (1, 'Devang', 'geekdevang@gmail.com', 'BCA', '6',
   'http://codejuris.com/BITConnect/TEst/./images/userpics/va9zjvm7o313cryq9rxb.jpg',
   'cK9LBScYR-M:APA91bGydQde9rPvwwhN1LEKwobYl80PbtOo8P0a2p7VV4Wy66mVWFaLvw3SRFI_sexGLkXdVUy14JygxCSxWB2LkYjUEcrUy6puWILWEeKcsFTb-Wpv5_rDdOUiNDdN0_Ap7_fHhxOi',
   '1483423060'),
  (2, 'BOT', 'devangchirag@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/./images/userpics/2hfo4esgggqc7lqw2jhf.jpg', 'dx-WcHz9moU:APA91bFRayQ2gJSYwZbTA1PMd7KWVxKUKw5HVSJG2KXAcLyGUigy2XiElT0MWOU2IgeGw5ZdzFBe_jiT_B6AzJV7NngghsS4NhcSw6oC6pcEKJVQAPZrNNb75e4fBMrUvs3Jl0egRav8', '1483437901'),
  (10, 'Saransh Agarwal', '96rishi@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'fBguFXRf0Co:APA91bHTSqAjiJUPKdCTEd4utET3leJhTNhG2W-TSu4aslQ4kyUGbSjj_uhEdFAIb4CrizB8uyz1ulUu4_Xy-fh_RPr_xhhWqxC0OIJ8erzGM6T0jwXrmNVOdHWYcq_AVs4paQSl-RIt', '1484486342'),
  (6, 'Palak Kulshrestha', 'palakkulshrestha@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/./images/userpics/po66382y6jjhb6qdz1or.jpg', 'ekoAY5KStaQ:APA91bGbGv-IM3cJOYsn2bCKi-amXUmvQdMCfvG4-l5Qg7T1QePoElHt4N18vyEzAurZkor7Bj2WtFkeg-3vMpvT6AMf38_-Kx042ciFdvZD78S0AeSpReREf29CBfn1g4zD4u_MTW65', '1483986125'),
  (11, 'Akshay Arora', 'akshayarora1431@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'fhvDUFU9McA:APA91bGMAwaRNmjQHbkOEFRmp_5tMbn5p2ejaRLF-T8R2PgLJbOZWk_EJwhEKhBG9rsGmZBVMmUg8m28VlBEawcJ1BiKFn3WO5xL1eWQIWy9WPZpBOqJFLmPIRfSL9d6e0AjOOMefTvN', '1484489870'),
  (12, 'Renu Chhajed', 'renuchhajed@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'cLHIFvXWvfQ:APA91bFLi8EH9rLp7j41eT0cE7WxlvKBrrlkkYAGy9-pn9dB43X899uBhKdRw9j9qSP5bf3NfAvFpFFZ5WgCoSszZAUkj7AJ86xuNjJAdsOE9wc18TzfRCkY4HSb9Te0pb35oGvNtd21', '1484494280'),
  (8, 'arpit mathur', 'arpitmth@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/./images/userpics/41dio4n0hphmzsp6v6n0.jpg', 'fW26KobuYFY:APA91bE3RqCxu4w46I4aqsZ6PbHnaFnWY5vdvJJdq7_KbF1VOVg_cGot5fG8VsC6WqHOoSaOy0r7cmhhppSn01ScnHBRiy2ZNFCJbVLbGdDfylVXgBUZzsO0ZqIU966TYa91soAK_8xk', '1484020029'),
  (13, 'maheshwari.mrinal@gmail.com', 'maheshwari.mrinal@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'ch87jSbFFGM:APA91bFqkYsWGy1J4aAi-V6XQ3vePjMWgFc1nAlKalYLC3Faop7kGyUkJmGjm6FlxmKFKXOiOSGfa-z8ATGcs8bgboHAE0SGAw-iOALVlyUGkMlaCGXDhRR9ZaJblhdDpLev-s6qTW7g', '1484498887'),
  (14, 'Suny Manchandia', 'sunymanchandia@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'e6qJ9rsn-6k:APA91bHHIjV7sJdlf6DZ8oBP1ssXVmBa2v3ubtmwfFDz6xFQHho1lDKbI4lfjYbwH4S8Ai52Wox6Ux4gclOreyZAd9LzF9q6w34ph6RUM73a8k03tp6hasMR0MKD5pvVM5OwfWtcvKsS', '1484560753'),
  (15, 'klhushal agarwal', 'meenkhushal@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', '', '1484718766'),
  (16, 'Tapish Jain', 'tapish07@gmail.com', 'BE-ECE', '6', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'eJCMrwodMa8:APA91bH9lsX6NwEkx3CR-3af2vywGSatDYAjOCLBkFO_GcVszaOHjyRtsiTpVCROU4R4t9CztY6CXE1Iy72SofL2m6x6MXT2k-scFpwNqydGyOhBxPov5zXWOembQVKj9P-J3MtDlklu', '1484737830'),
  (18, 'Abhineet Bhargava', 'abhineetbhargava95@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'dcFTfDCGR-8:APA91bHH0jwKR_XCfOXQxFvK4N251U3nKaQ2YUANy3PXWu1ZJZzYqKpnnqDxzStGMeg-hwyOUYCP66GXvjieYhVa6zMbQcojAkikDqXGh4fhNqDkRL7wTZG5Me3M-_pC3iXv6FjfqoVz', '1485622059'),
  (22, 'Mrinal Maheshwari', 'maheshwariswati69@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'dz7g5yryLRI:APA91bGLqrCAlC8nFx4Q0N6QrvLGAZ_fKkIRTUQr81brOe0B8Wlehb445ZZZa5HQ0TI7NN_2RItHxnqDWudCXJZPWVk5mlnJlv3KfWHHFZChcrjT7Rft64eE91ITYIL_jXTHgEqI2i7F', '1486483704'),
  (21, 'ARPIT MATHUR', 'iarpit1095@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'eNoLz7EkHUc:APA91bEgRhQ2vCOs7gwJHufAjoCbvwlzDGqx3Q5zixqf3k9HTVbB2DpgSjTv7t7iZKMma-JoaZD6-NezdSXW0dbxBDYQTMP91Ux-j0yOtOBm6zgftqLgiEYyTwQnO4aj94kWdVOLYygf', '1486439432'),
  (23, 'Ujjwal sinha', 'ankur.sinha470@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'd_9hFdxpfAw:APA91bEVQxhctinsQcJ6Xa2e3Vbzbh9Hfe-u6RGzyYa6pkJO5NKk68OUoxWfwMWTY2q5TbFDID-KQLwQRvX-8APfd_KF8WWknfWqHN0AlCRvcrea2tb5kWqnb-z4jrkCecVjcKRlkbfO', '1486607483'),
  (24, 'babu soban', 'soban1985@gmail.com', 'BCA', '1', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'eMJgRUjohfw:APA91bGUkogd7lKnPuOB0THwRVb5jANEePfTFqsFr4ml8AtoghKajSx6P2Zff4IjU1Y_5iw--fAtgNCjJUWkuE426uQMgGW8KdxwvrM9DTnafO2xZUmVzCbj37e9_ktvdx1LRu_sKbqQ', '1486740061'),
  (25, 'Pulkit Gupta', 'pulkitgupta040@gmail.com', 'BE-CSE', '6', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'dr1bLFsvq0w:APA91bHJlB4cQkLuW77kYd_A2TAhRDRpAnvmkrF63ZOAOM-jrOubmPRN-Ls7JF-0mTrxZhHbF2tsQ1133LFH9UcU-11e_-N2WF1If3j0iRdnP2iCkbgMRnCxR4dXktm2sO_zoUmv7q6R', '1486753898'),
  (26, 'mehul vora', 'voramehul108@gmail.com', 'BCA', '1', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'ehxZXMFRbmA:APA91bFJTwdB-uFjJSpj0cIJe4AcR6xXwo_SsNrK7ddUgBPG2sjSELkBAlyDXVTj1U4Hi1at7cycfe33Qz0Lgcw5BHDQ7vtS04a9c1IKXiTXw51KxEiOcnfnyTU1S7FAjZz6EgBVPWxO', '1487253087'),
  (27, 'BCA 5', 'bitbca25@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'fX3rlmcVQJw:APA91bG0KMT8vuuSrNDPQQObcQdZsOJQArrDbmLqUiE8XOKeguW9XR7diy5LRyO5sMGMnZkxBUoRr5VvcFKZk3VxFEC67GaXE-IuYea4SyjFkZJHFySPBNFhKMk_wWa6_BcRgGmLERx0', '1487416690'),
  (28, 'mohit jain', 'mjain3647@gmail.com', 'BCA', '5', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'e_QASwAklgA:APA91bFn_cO6bpC8fxnbMmKPrY0ez5XfOW9_qgnPDpM9gazwX5hry2D70930gyztaqJx6kkpLMIn1O2iqvs6M3UwrNYUUYmlglm0NcEYCiSvTBPN4NpsNDxYmJgcZvTkQFDc9KKedZe7', '1487511800'),
  (29, 'Kireet Sharma', 'talktokireet@gmail.com', 'BCA', '6', 'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg', 'c4Zqjn6Hxow:APA91bGUH6EZfrFaa9mRU-GuxbhYVPShzcL2xMhvCwM7H2dX8wO11dlhqAeUMc6Vow6nM0UM9dXaxcEFKpOJoU-EEuchaKsE9aU02vB__PgXTbk4q895GuDfy032Kt7mg_EL1QG6ppXz', '1487607169'),
  (30, 'Himakshiba Gohil', 'himakshibagohil23@gmail.com', 'BCA', '6',
   'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg',
   'fUbPd-Rug0U:APA91bFy79dmIDvya_KQOKT8RXWu1vgGznFKCU9TSF8O7Bm10n__A7EIs_bAXKwF0BqyrqggJNwUlNu4HNucs4D2vOerqJh_Ihsw-VoVOaxRnIIYUetlA_F19eGD2EM2aFow6oWFc47d',
   '1487709184'),
  (31, 'Priyanka Gupta', 'guptapriyanka1708.pg@gmail.com', 'BCA', '6',
   'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg',
   'cg1T2sTF-qs:APA91bE_HlqKs7Y86sFP8HbWm4n6TXR8rvwYLzGrrbM8NSyz06nLU93jaeUIL1mZEWyeJl-9NYckukr87Xd4ZrzuUE0vvp1dFOnbLgoYxWpkABlni2nlY5GiWmklce0XG_s9U4UCc_cr',
   '1487870306'),
  (32, 'Siddhant Srivastava', 'callmesid1@gmail.com', 'BCA', '6',
   'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg',
   'f5Gq7Jevm3o:APA91bGTBM6L-DdFGP2TWF3uN9EhuIopU7gulZ6CPhoTFVGeJkyoDIQThvy0RwTdvK6avEq0cZWXg0JIoKpul9qIE5doyiDyLouoWBorW8MyROLyID1jBi2V9cgAB00FMrICmQzvN-7R',
   '1488073571'),
  (33, 'Rafi Ansari', 'sam9999955@gmail.com', 'BCA', '1',
   'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg',
   'cHOmZxfcV40:APA91bGcGkOOxv4dJSoKK_JCtnlPLyNd_vb-95DK7-9P_GihAsPuCUYqPSA8vV0tyDa_p45FSrrl2_Nbdyi8R2oLw0RXIE5dj2Wxk5-6CUFMTzUDpzwQhEXmvlGtvKCWT_EbVEDOa9Bc',
   '1488100561'),
  (34, 'Divyansh jain', 'divyansh.jain24@gmail.com', 'BBA', '6',
   'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg',
   'eqOlXWvlHoY:APA91bECPkNi0Gl3GKYKnFG1WsSEeZi5bLu3YkuSEvR1mDO8_8raLsVwves_5Lg0ubK4_RKnMfjd4Jkq-CEZD7E7phcqbyPmrBTzmiUD7I1OYIRCFZfgn4IMRX0wJgSHx5EErn4Mo7gL',
   '1488882287'),
  (35, 'Amit Rana', 'amitrana078@gmail.com', 'BCA', '1',
   'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg',
   'cRzFKpfMv-U:APA91bGvzAl5FGAZiQ16fT575d-e01MxFB8ivRabSuiJ8TW2pEBkypl5vOwBTm78j0D9Ny8WncJH7nC7BPSFAR8aPpZQOG2newV22-MqU9Vj0CmNJuxVUMbEk5GlSndZ7K2opKLkAlfg',
   '1489119875'),
  (36, 'Aditya Soni', 'adist98@gmail.com', 'BE-ECE', '1',
   'http://codejuris.com/BITConnect/TEst/images/app/default_user_pic.jpg',
   'eOrU9N5L4So:APA91bEBWtwDjXJlgD4w99PEhsO0M0n0OXX-6-YTz0FgOx5PegvLAmWnW7ndpaEpcP0z8mu8jlgrQMQZbWpMWhVPfUTWHPjfTN5UTIMYS599cqh-XNuWxy6I1ZQCQrEoZ0gWWAH7OH3Z',
   '1489774860');

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
