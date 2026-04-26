SELECT 
    s.submission_date,

    COUNT(DISTINCT s.hacker_id) AS total_hackers,

    (
        SELECT s2.hacker_id
        FROM Submissions s2
        WHERE s2.submission_date = s.submission_date
        GROUP BY s2.hacker_id
        ORDER BY COUNT(*) DESC, s2.hacker_id
        LIMIT 1
    ) AS hacker_id,

    (
        SELECT h.name
        FROM Hackers h
        WHERE h.hacker_id = (
            SELECT s3.hacker_id
            FROM Submissions s3
            WHERE s3.submission_date = s.submission_date
            GROUP BY s3.hacker_id
            ORDER BY COUNT(*) DESC, s3.hacker_id
            LIMIT 1
        )
    ) AS name

FROM Submissions s

WHERE (
    SELECT COUNT(DISTINCT s2.submission_date)
    FROM Submissions s2
    WHERE s2.hacker_id = s.hacker_id
    AND s2.submission_date <= s.submission_date
) = DATEDIFF(s.submission_date, '2016-03-01') + 1

GROUP BY s.submission_date
ORDER BY s.submission_date;
