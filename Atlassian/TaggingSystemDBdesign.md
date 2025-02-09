# Database Design for Tagging System in Confluence

## 1. Pages Table
Stores information about Confluence pages.

| Column Name     | Data Type      | Description                        |
|------------------|----------------|------------------------------------|
| `page_id`       | UUID (PK)      | Unique identifier for the page.    |
| `title`         | VARCHAR(255)   | Title of the page.                 |
| `created_at`    | TIMESTAMP      | Timestamp of when the page was created. |

## 2. Tags Table
Stores tags that can be assigned to pages.

| Column Name     | Data Type      | Description                       |
|------------------|----------------|-----------------------------------|
| `tag_id`        | UUID (PK)      | Unique identifier for the tag.    |
| `tag_name`      | VARCHAR(100)   | Name of the tag.                  |

## 3. Page_Tags Table
Associates tags with pages (Many-to-Many relationship).

| Column Name     | Data Type      | Description                       |
|------------------|----------------|-----------------------------------|
| `page_id`       | UUID (FK)      | Reference to `Pages.page_id`.     |
| `tag_id`        | UUID (FK)      | Reference to `Tags.tag_id`.       |

## 4. Page_Interactions Table
Tracks likes, dislikes, and views for each page.

| Column Name     | Data Type      | Description                       |
|------------------|----------------|-----------------------------------|
| `interaction_id`| UUID (PK)      | Unique identifier for the interaction. |
| `page_id`       | UUID (FK)      | Reference to `Pages.page_id`.     |
| `user_id`       | UUID (FK)      | Identifier for the user interacting. |
| `interaction_type` | ENUM('like', 'dislike', 'view') | Type of interaction. |
| `interaction_date` | TIMESTAMP    | Date and time of the interaction. |

## 5. Aggregated_Interactions View (Optional)
A materialized or dynamic view to pre-calculate interaction counts for each page.

| Column Name     | Data Type      | Description                        |
|------------------|----------------|------------------------------------|
| `page_id`       | UUID           | Reference to `Pages.page_id`.      |
| `likes_count`   | INT            | Total likes for the page.          |
| `dislikes_count`| INT            | Total dislikes for the page.       |
| `views_count`   | INT            | Total views for the page.          |
| `total_interactions` | INT       | Sum of likes, dislikes, and views. |

---

## SQL Queries

### 1. Track Interactions
To add a new interaction:
```sql
INSERT INTO Page_Interactions (interaction_id, page_id, user_id, interaction_type, interaction_date)
VALUES (gen_random_uuid(), 'page-id-123', 'user-id-456', 'like', NOW());
```

### 2. Find the Most Interacted Page
Aggregate interactions and find the page with the highest total interactions:
```sql
SELECT p.page_id, p.title, 
       COUNT(CASE WHEN pi.interaction_type = 'like' THEN 1 END) AS likes_count,
       COUNT(CASE WHEN pi.interaction_type = 'dislike' THEN 1 END) AS dislikes_count,
       COUNT(CASE WHEN pi.interaction_type = 'view' THEN 1 END) AS views_count,
       COUNT(*) AS total_interactions
FROM Pages p
LEFT JOIN Page_Interactions pi ON p.page_id = pi.page_id
GROUP BY p.page_id, p.title
ORDER BY total_interactions DESC
LIMIT 1;
```

### 3. Get Tags for a Page
Fetch all tags for a given page:
```sql
SELECT t.tag_name
FROM Page_Tags pt
JOIN Tags t ON pt.tag_id = t.tag_id
WHERE pt.page_id = 'page-id-123';
```

### 4. Get Interactions by Tag
Fetch the total interactions for pages under a specific tag:
```sql
SELECT t.tag_name, 
       COUNT(pi.interaction_id) AS total_interactions
FROM Tags t
JOIN Page_Tags pt ON t.tag_id = pt.tag_id
JOIN Page_Interactions pi ON pt.page_id = pi.page_id
WHERE t.tag_name = 'example-tag'
GROUP BY t.tag_name;
```

---

## Scaling Considerations

1. **Indexes:**
   - Add indexes on `Page_Interactions.page_id`, `Page_Interactions.interaction_type`, and `Page_Interactions.interaction_date` for faster aggregation.
   - Index `Page_Tags.page_id` and `Tags.tag_name` for efficient joins.

2. **Sharding:**
   - For large-scale systems, consider sharding `Page_Interactions` by `page_id`.

3. **Caching:**
   - Use a caching layer (e.g., Redis) to store frequently queried results like "most interacted page."

4. **Materialized Views:**
   - Use materialized views for pre-aggregating interaction counts to reduce query latency.
