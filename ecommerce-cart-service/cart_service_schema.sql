-- E-COMMERCE CART SERVICE - DATABASE SCRIPT
-- Based on ecommerce_clean_notes.sql (MySQL 8.0.34).
-- Adds Cart tables only; does not drop or modify existing tables/data.
-- Run after the existing ecommerce database script.

USE `ecommerce`;

-- 1. Cart status master
CREATE TABLE IF NOT EXISTS `cart_status` (
  `status_id` varchar(10) NOT NULL,
  `status_name` varchar(30) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`status_id`),
  UNIQUE KEY `uk_cart_status_name` (`status_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `cart_status` (`status_id`,`status_name`,`description`,`is_active`) VALUES
('CST001','ACTIVE','Cart is open and can be modified',1),
('CST002','CHECKED_OUT','Cart has been converted into an order',1),
('CST003','ABANDONED','Cart was not checked out',1),
('CST004','CANCELLED','Cart was cancelled',1)
ON DUPLICATE KEY UPDATE
  `description`=VALUES(`description`), `is_active`=VALUES(`is_active`);

-- 2. Cart header: one cart belongs to one existing ecommerce user.
-- A user may have historical carts; service logic should allow only
-- one ACTIVE cart per user at a time.
CREATE TABLE IF NOT EXISTS `cart` (
  `cart_id` varchar(20) NOT NULL,
  `user_id` varchar(15) NOT NULL,
  `status_id` varchar(10) NOT NULL DEFAULT 'CST001',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cart_id`),
  KEY `idx_cart_user_status` (`user_id`,`status_id`),
  KEY `idx_cart_status` (`status_id`),
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`)
    REFERENCES `oprauth_users` (`user_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_cart_status` FOREIGN KEY (`status_id`)
    REFERENCES `cart_status` (`status_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 3. Cart item detail: product_item is the selected SKU/variant.
-- variation_id is nullable because the supplied SQL defines
-- product_variation but contains no seed rows for it.
-- unit_price is a snapshot; checkout must revalidate price and stock.
CREATE TABLE IF NOT EXISTS `cart_item` (
  `cart_item_id` varchar(20) NOT NULL,
  `cart_id` varchar(20) NOT NULL,
  `product_item_id` varchar(7) NOT NULL,
  `variation_id` varchar(7) DEFAULT NULL,
  `quantity` int NOT NULL DEFAULT 1,
  `unit_price` decimal(12,2) NOT NULL,
  `added_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cart_item_id`),
  UNIQUE KEY `uk_cart_item_cart_product_item` (`cart_id`,`product_item_id`),
  KEY `idx_cart_item_product_item` (`product_item_id`),
  KEY `idx_cart_item_variation` (`variation_id`),
  CONSTRAINT `chk_cart_item_quantity` CHECK (`quantity` > 0),
  CONSTRAINT `chk_cart_item_unit_price` CHECK (`unit_price` >= 0),
  CONSTRAINT `fk_cart_item_cart` FOREIGN KEY (`cart_id`)
    REFERENCES `cart` (`cart_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_cart_item_product_item` FOREIGN KEY (`product_item_id`)
    REFERENCES `product_item` (`pro_item_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_cart_item_variation` FOREIGN KEY (`variation_id`)
    REFERENCES `product_variation` (`variation_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ID generation is handled by Cart Service (UUID/ULID or a
-- concurrency-safe sequence), not MAX(id)+1 triggers.
-- Example IDs: CART000000000001 and CITEM000000000001.

-- Relationships:
-- oprauth_users 1 --- many cart
-- cart_status   1 --- many cart
-- cart          1 --- many cart_item
-- product       1 --- many product_item
-- product_item  1 --- many cart_item
-- product_variation 1 --- many cart_item (optional)
--
-- Product details are obtained by joining:
-- cart_item -> product_item -> product -> product_category.
-- The service must validate that variation_id belongs to the
-- selected product_item_id; independent FKs cannot enforce that pair.
--
-- Validation:
-- SHOW TABLES LIKE 'cart%';
-- SELECT TABLE_NAME, COLUMN_NAME, CONSTRAINT_NAME,
--        REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME
-- FROM information_schema.KEY_COLUMN_USAGE
-- WHERE TABLE_SCHEMA=DATABASE()
--   AND TABLE_NAME IN ('cart','cart_item')
--   AND REFERENCED_TABLE_NAME IS NOT NULL
-- ORDER BY TABLE_NAME, COLUMN_NAME;
