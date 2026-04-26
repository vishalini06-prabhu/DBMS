SELECT 
CASE 
    WHEN g.grade < 8 THEN NULL 
    ELSE s.name 
END AS name,
g.grade,
s.marks

FROM Students s
JOIN Grades g
ON s.marks BETWEEN g.min_mark AND g.max_mark

ORDER BY 
g.grade DESC,
CASE WHEN g.grade >= 8 THEN s.name END,
CASE WHEN g.grade < 8 THEN s.marks END;
