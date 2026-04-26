SELECT 
c.contest_id,
c.hacker_id,
c.name,
COALESCE(SUM(ss.total_submissions),0),
COALESCE(SUM(ss.total_accepted_submissions),0),
COALESCE(SUM(vs.total_views),0),
COALESCE(SUM(vs.total_unique_views),0)
FROM Contests c

JOIN Colleges col 
    ON c.contest_id = col.contest_id

JOIN Challenges ch 
    ON col.college_id = ch.college_id
