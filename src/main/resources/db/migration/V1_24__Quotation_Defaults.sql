DELETE from `setting` WHERE `name`='DEFAULT_QUOTATION_EXPIRATION_DATE';

INSERT INTO `setting` VALUES 
('DEFAULT_QUOTATION_EXPIRATION_DATE', '3', 'Default Number of days for quotation expiration', '2', '90');
