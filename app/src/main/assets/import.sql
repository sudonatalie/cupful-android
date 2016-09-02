-- Import foods with volumes in cups
INSERT INTO ingredient (NDB_No, name, gPerCup)
SELECT FOOD_DES.NDB_No, FOOD_DES.Long_Desc, WEIGHT.Gm_Wgt / WEIGHT.Amount
FROM FOOD_DES INNER JOIN WEIGHT ON FOOD_DES.NDB_No = WEIGHT.NDB_No
WHERE WEIGHT.Msre_Desc = "cup";

-- Import foods with volumes in tbsp (16 tbsp / cup)
INSERT INTO ingredient (NDB_No, name, gPerCup)
SELECT FOOD_DES.NDB_No, FOOD_DES.Long_Desc, WEIGHT.Gm_Wgt / WEIGHT.Amount * 16.0
FROM FOOD_DES INNER JOIN WEIGHT ON FOOD_DES.NDB_No = WEIGHT.NDB_No
WHERE WEIGHT.Msre_Desc = "tbsp";

-- Import foods with volumes in fl oz (8 fl oz / cup)
INSERT INTO ingredient (NDB_No, name, gPerCup)
SELECT FOOD_DES.NDB_No, FOOD_DES.Long_Desc, WEIGHT.Gm_Wgt / WEIGHT.Amount * 8.0
FROM FOOD_DES INNER JOIN WEIGHT ON FOOD_DES.NDB_No = WEIGHT.NDB_No
WHERE WEIGHT.Msre_Desc = "fl oz";

-- Import foods with volumes in tsp (48 tsp / cup)
INSERT INTO ingredient (NDB_No, name, gPerCup)
SELECT FOOD_DES.NDB_No, FOOD_DES.Long_Desc, WEIGHT.Gm_Wgt / WEIGHT.Amount * 48.0
FROM FOOD_DES INNER JOIN WEIGHT ON FOOD_DES.NDB_No = WEIGHT.NDB_No
WHERE WEIGHT.Msre_Desc = "tsp";