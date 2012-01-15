-- phpMyAdmin SQL Dump
-- version 2.11.8.1deb5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 04, 2009 at 01:18 AM
-- Server version: 5.0.51
-- PHP Version: 5.2.6-1+lenny2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `tobo`
--

-- --------------------------------------------------------

--
-- Table structure for table `deleted_todos`
--

CREATE TABLE IF NOT EXISTS `deleted_todos` (
  `id` int(11) NOT NULL auto_increment,
  `pos` int(11) NOT NULL,
  `jid` varchar(200) character set latin1 NOT NULL,
  `todo` text character set latin1 NOT NULL,
  `status` varchar(1) character set latin1 NOT NULL default 'I',
  `ts` timestamp NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=410 ;

-- --------------------------------------------------------

--
-- Table structure for table `reminders`
--

CREATE TABLE IF NOT EXISTS `reminders` (
  `id` int(11) NOT NULL auto_increment,
  `todo_id` int(11) NOT NULL,
  `rdate` timestamp NOT NULL default '0000-00-00 00:00:00',
  `processed` tinyint(1) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=10 ;

-- --------------------------------------------------------

--
-- Table structure for table `todos`
--

CREATE TABLE IF NOT EXISTS `todos` (
  `id` int(11) NOT NULL auto_increment,
  `pos` int(11) NOT NULL,
  `jid` varchar(200) character set latin1 NOT NULL,
  `todo` text character set latin1 NOT NULL,
  `status` varchar(1) character set latin1 NOT NULL default 'I',
  `ts` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=417 ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL auto_increment,
  `jid` varchar(200) character set latin1 NOT NULL,
  `active` tinyint(1) NOT NULL default '1',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=4 ;
