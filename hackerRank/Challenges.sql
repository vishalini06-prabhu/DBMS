SELECT h.hacker_id, h.name, COUNT(c.challenge_id) AS total
FROM Hackers h
JOIN Challenges c 
    ON h.hacker_id = c.hacker_id
GROUP BY h.hacker_id, h.name
HAVING 
    total = (
        SELECT MAX(cnt) 
        FROM (
            SELECT COUNT(*) AS cnt
            FROM Challenges
            GROUP BY hacker_id
        ) x
    )
    OR total IN (
        SELECT cnt FROM (
            SELECT COUNT(*) AS cnt
            FROM Challenges
            GROUP BY hacker_id
        ) y
        GROUP BY cnt
        HAVING COUNT(*) = 1
    )
ORDER BY total DESC, h.hacker_id;
