INSERT INTO biz_phonebook (merchant_name, owner_name, phone, phone1, phone2, category, address, business_license, status, create_by, create_time)
SELECT 
    merchant_name, 
    owner_name, 
    phone, 
    phone, 
    NULL, 
    category, 
    address, 
    business_license, 
    '0', 
    approve_by, 
    approve_time
FROM biz_phonebook_apply 
WHERE apply_status = '1';
