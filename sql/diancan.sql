/*
Navicat MySQL Data Transfer

Source Server         : diancan
Source Server Version : 50721
Source Host           : 192.168.1.160:3306
Source Database       : diancan

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-03-22 16:24:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for dc_administrators
-- ----------------------------
DROP TABLE IF EXISTS `dc_administrators`;
CREATE TABLE `dc_administrators` (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_account` varchar(20) DEFAULT NULL,
  `admin_password` varchar(50) DEFAULT NULL,
  `admin_rights` int(11) DEFAULT NULL,
  `admin_isDel` int(11) DEFAULT NULL,
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dc_board
-- ----------------------------
DROP TABLE IF EXISTS `dc_board`;
CREATE TABLE `dc_board` (
  `board_id` int(20) NOT NULL AUTO_INCREMENT,
  `board_name` varchar(50) DEFAULT NULL,
  `board_people_number` int(11) DEFAULT NULL,
  `board_type` int(1) NOT NULL,
  `board_isDel` int(11) DEFAULT NULL,
  PRIMARY KEY (`board_id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dc_contact
-- ----------------------------
DROP TABLE IF EXISTS `dc_contact`;
CREATE TABLE `dc_contact` (
  `contact_id` int(11) NOT NULL AUTO_INCREMENT,
  `contact_phone` varchar(20) DEFAULT NULL,
  `contact_email` varchar(30) DEFAULT NULL,
  `contact_address` varchar(40) DEFAULT NULL,
  `contact_workTime` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`contact_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dc_coupon
-- ----------------------------
DROP TABLE IF EXISTS `dc_coupon`;
CREATE TABLE `dc_coupon` (
  `coupon_id` int(20) NOT NULL AUTO_INCREMENT,
  `consumption_amount` int(11) DEFAULT NULL,
  `discount` int(11) DEFAULT NULL,
  `expiry_time` datetime DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  `coupon_isDel` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  PRIMARY KEY (`coupon_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dc_food
-- ----------------------------
DROP TABLE IF EXISTS `dc_food`;
CREATE TABLE `dc_food` (
  `food_id` int(20) NOT NULL AUTO_INCREMENT,
  `food_name` varchar(20) DEFAULT NULL,
  `food_type_id` int(11) DEFAULT NULL,
  `food_price` decimal(10,2) DEFAULT NULL,
  `food_remark` varchar(200) DEFAULT NULL,
  `food_grounding` int(11) DEFAULT NULL,
  `food_monthlysales` int(11) DEFAULT NULL,
  `food_vip_price` decimal(10,2) DEFAULT NULL,
  `food_isDel` int(11) unsigned zerofill DEFAULT '00000000000',
  `food_img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`food_id`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dc_foodtype
-- ----------------------------
DROP TABLE IF EXISTS `dc_foodtype`;
CREATE TABLE `dc_foodtype` (
  `foodtype_id` int(11) NOT NULL AUTO_INCREMENT,
  `foodtype_name` varchar(20) DEFAULT NULL,
  `foodtype_isDel` int(11) DEFAULT NULL,
  PRIMARY KEY (`foodtype_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dc_order
-- ----------------------------
DROP TABLE IF EXISTS `dc_order`;
CREATE TABLE `dc_order` (
  `order_id` varchar(50) NOT NULL,
  `board_id` int(11) DEFAULT NULL,
  `order_people_number` int(11) DEFAULT NULL,
  `order_date` datetime DEFAULT NULL,
  `order_board_date` date DEFAULT NULL,
  `order_board_time_interval` varchar(20) DEFAULT NULL,
  `order_total_amount` decimal(10,2) DEFAULT NULL,
  `order_status` varchar(10) DEFAULT NULL,
  `user_id` varchar(50) DEFAULT NULL,
  `coupon_id` varchar(50) DEFAULT NULL,
  `order_food` varchar(80) DEFAULT NULL,
  `order_food_num` varchar(80) DEFAULT NULL,
  `order_paid` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dc_recharge
-- ----------------------------
DROP TABLE IF EXISTS `dc_recharge`;
CREATE TABLE `dc_recharge` (
  `recharge_id` varchar(50) NOT NULL,
  `recharge_amt` decimal(10,2) DEFAULT NULL,
  `recharge_date` datetime DEFAULT NULL,
  `recharge_user` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`recharge_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dc_sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `dc_sys_login_log`;
CREATE TABLE `dc_sys_login_log` (
  `ll_id` int(20) NOT NULL AUTO_INCREMENT,
  `ll_login_date` datetime DEFAULT NULL,
  `ll_ip` varchar(15) DEFAULT NULL,
  `ll_req_url` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ll_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15580 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dc_sys_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `dc_sys_operate_log`;
CREATE TABLE `dc_sys_operate_log` (
  `ol_id` int(20) NOT NULL AUTO_INCREMENT,
  `ol_type` varchar(100) DEFAULT NULL,
  `ol_module` varchar(100) DEFAULT NULL,
  `ol_content` varchar(500) DEFAULT NULL,
  `admin_id` int(11) DEFAULT NULL,
  `ol_add_date` datetime DEFAULT NULL,
  `ol_ip` varchar(15) DEFAULT NULL,
  `ol_remarks` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ol_id`)
) ENGINE=InnoDB AUTO_INCREMENT=363 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dc_user
-- ----------------------------
DROP TABLE IF EXISTS `dc_user`;
CREATE TABLE `dc_user` (
  `user_id` varchar(50) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `user_phone` varchar(20) DEFAULT NULL,
  `accumulate_points` int(11) NOT NULL,
  `balance` decimal(10,2) NOT NULL,
  `member_level` varchar(20) NOT NULL,
  `user_is_del` int(11) DEFAULT NULL,
  `user_password` varchar(50) DEFAULT NULL,
  `user_coupon` varchar(300) DEFAULT '1',
  `user_card_no` varchar(6) DEFAULT NULL,
  `user_gender` int(1) DEFAULT NULL,
  `user_birth` date DEFAULT NULL,
  `user_register_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_phone` (`user_phone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
