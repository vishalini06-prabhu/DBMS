SELECT 
    SUM(c.population) AS total_population
FROM 
    CITY c
JOIN 
    COUNTRY co 
    ON c.CountryCode = co.Code
WHERE 
    co.Continent = 'Asia';
