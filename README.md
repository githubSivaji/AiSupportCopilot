
````markdown
# AI Support Copilot

An AI-powered customer support backend built with Java and Spring Boot that combines
LLM-powered conversations, Retrieval-Augmented Generation (RAG), vector search,
agent-based tool calling, persistent conversation history, and secure business
operations.

The system allows customers to interact with an AI support agent that can answer
questions from company knowledge bases, retrieve order information, and create
support tickets while enforcing user-level authorization.

---

## 🚀 Project Overview

AI Support Copilot is designed as a backend platform for building intelligent
customer-support workflows.

Instead of relying only on an LLM's general knowledge, the system combines:

- Company knowledge stored in PostgreSQL
- Semantic search using pgvector
- Hybrid keyword + vector retrieval
- Retrieval-Augmented Generation (RAG)
- Google Gemini LLM
- LangChain4j agent orchestration
- Backend tools for business operations
- Persistent conversation history
- Conversation-specific AI memory
- JWT authentication and authorization
- Circuit breaker and caching for reliable LLM communication

### High-Level Architecture

```text
                         Client
                           |
                           v
                  Spring Boot REST API
                           |
                           v
                  Authentication Layer
                  Spring Security + JWT
                           |
                           v
                  Agent Controller
                           |
                           v
                 Orchestrator Service
                           |
                +----------+----------+
                |                     |
                v                     v
          LangChain4j Agent       LLM Gateway
                |                     |
        +-------+-------+             v
        |       |       |          Google Gemini
        v       v       v
       RAG    Order   Ticket
      Tool    Tool     Tool
        |
        v
   Hybrid Search
    |          |
    v          v
Keyword      pgvector
Search       Semantic Search
    |          |
    +----+-----+
         |
         v
    PostgreSQL
         |
    +----+------------------+
    |                       |
    v                       v
Business Data          Conversation
                       + Message History
````

---

# ✨ Key Features

## 1. Authentication & Authorization

Implemented secure authentication using:

* Spring Security
* JWT
* Role-based authorization
* User-level resource ownership validation

Protected operations ensure that users can only access their own:

* Orders
* Conversations
* Support tickets

Example:

```text
User A
   |
   +----> Order A       ✅ Allowed
   |
   +----> Order B       ❌ Unauthorized
```

---

## 2. RESTful Backend

Built using a layered Spring Boot architecture:

```text
Controller
    |
Service
    |
Repository
    |
PostgreSQL
```

The backend separates responsibilities across:

* Controllers
* DTOs
* Services
* Repositories
* Entities
* Security
* AI Gateway
* RAG
* Agents
* Tools
* Memory

---

# 🤖 Generative AI

## Google Gemini Integration

Google Gemini is used as the primary LLM.

The application integrates Gemini through:

* Spring AI
* LangChain4j
* Google GenAI

The project uses Gemini for:

* Customer conversations
* RAG-generated answers
* Agent reasoning
* Tool selection
* Natural-language responses

---

# 🧠 LLM Gateway

A centralized LLM Gateway is used to avoid coupling the application directly
to the Gemini API.

```text
Application
     |
     v
 LLM Gateway
     |
     v
 Gemini
```

Responsibilities include:

* Centralized LLM communication
* Prompt handling
* Response caching
* Failure handling
* Circuit breaker protection

### Circuit Breaker

Resilience4j is used to protect the application from repeated LLM failures.

Configuration:

```text
Sliding Window       : 10 calls
Failure Threshold    : 50%
Minimum Calls        : 5
Open State Duration  : 10 seconds
Half Open Calls      : 2
```

Fallback response is returned when the Gemini service becomes unavailable.

---

# 📚 RAG - Retrieval Augmented Generation

The project implements a complete RAG pipeline for answering questions using
company-specific knowledge.

```text
Document
   |
   v
Document Ingestion
   |
   v
Chunking
   |
   v
Embedding Generation
   |
   v
pgvector
   |
   v
User Question
   |
   v
Hybrid Retrieval
   |
   v
Relevant Context
   |
   v
Gemini
   |
   v
Grounded Answer
```

---

## Document Ingestion

Documents are stored in PostgreSQL and divided into smaller chunks.

Current chunk size:

```text
500 characters
```

Each chunk contains metadata such as:

* Document ID
* Chunk index

---

# 🔎 Semantic Search

The project uses PostgreSQL with the `pgvector` extension for vector search.

Embedding model:

```text
text-embedding-004
```

Vector dimension:

```text
768
```

Vector index:

```text
HNSW
```

Distance metric:

```text
Cosine Distance
```

This allows the application to find documents based on semantic meaning rather
than exact keyword matches.

Example:

```text
Question:
"Can I send the product back?"

        ↓

Semantic Search

        ↓

"Return Policy"
```

---

# 🔍 Hybrid Search

The system combines:

```text
Keyword Search
       +
Semantic Vector Search
       ↓
Hybrid Ranking
```

Current ranking approach:

```text
Keyword Score     = 0.4
Semantic Score    = 0.6
```

The system combines both scores and returns the top relevant results.

This helps handle both:

* Exact keyword queries
* Natural-language queries

---

# 🧩 LangChain4j Agent

LangChain4j is used to implement the support agent.

The agent can dynamically decide whether it needs to:

```text
Normal conversation
       |
       +----> Knowledge Base
       |
       +----> Order Tool
       |
       +----> Support Ticket Tool
```

The agent uses conversation-specific memory IDs:

```text
@MemoryId
    |
    v
conversationId
    |
    v
ChatMemory
```

This allows different conversations to maintain separate contexts.

---

# 🛠️ AI Tools

The agent currently has three major capabilities.

## 1. Knowledge Base Search

Allows the agent to search:

* FAQs
* Shipping policies
* Return policies
* Refund policies
* Company documentation

---

## 2. Order Status

The agent can retrieve order information using an order ID.

Example:

```text
User:
Where is my order 44444444-4444-4444-4444-444444444444?

        ↓

LangChain4j Agent

        ↓

Order Tool

        ↓

PostgreSQL

        ↓

Order Status

        ↓

Natural Language Response
```

The tool validates that the order belongs to the authenticated user.

---

## 3. Support Ticket Creation

The AI agent can create a support ticket when a customer needs additional
assistance.

Example:

```text
User:
My delivery is delayed.

        ↓

Agent

        ↓

Create Support Ticket

        ↓

PostgreSQL
```

---

# 💬 Conversation Management

The system supports persistent conversation management.

Each conversation contains:

```text
Conversation
     |
     +---- User Message
     |
     +---- Assistant Message
     |
     +---- User Message
     |
     +---- Assistant Message
```

Conversation states:

```text
ACTIVE
CLOSED
```

Users can:

* Create conversations
* Continue existing conversations
* View conversation history
* Close conversations
* Access only their own conversations

---

# 🧠 Conversation Memory

The project uses LangChain4j `ChatMemory` to maintain conversational context.

Each conversation is identified using:

```text
conversationId
```

Example:

```text
Conversation A

"My name is Sivaji"

        ↓

"What is my name?"

        ↓

"My name is Sivaji."
```

A different conversation does not inherit the previous conversation's memory.

---

# 🗄️ Persistent Chat Memory

LangChain4j memory is persisted using PostgreSQL through a custom:

```text
PostgresChatMemoryStore
```

Architecture:

```text
LangChain4j
      |
      v
ChatMemory
      |
      v
PostgresChatMemoryStore
      |
      v
PostgreSQL
```

The system maintains a distinction between:

```text
Conversation History
        |
        +---- Permanent application history

Chat Memory
        |
        +---- LLM working context
```

This allows the application to preserve the complete conversation history while
controlling the amount of context provided to the LLM.

---

# 🔐 Security Architecture

Authentication flow:

```text
Client
  |
  | Authorization: Bearer <JWT>
  v
JwtAuthenticationFilter
  |
  v
Validate JWT
  |
  v
Load User
  |
  v
Spring Security Context
  |
  v
Controller
```

User identity is propagated to AI tools through the application context.

This prevents the LLM from bypassing normal authorization rules.

Example:

```text
Authenticated User
       |
       v
Order Tool
       |
       v
Verify order.user == authenticatedUser
       |
    +--+--+
    |     |
   YES    NO
    |     |
   Data  DENY
```

---

# 🏗️ Project Structure

```text
src/main/java/com/sivaji/aisupportcopilot
│
├── ai
│   ├── agent
│   │   ├── SupportAgent.java
│   │   ├── SupportAgentImpl.java
│   │   └── SupportAgentTools.java
│   │
│   ├── gateway
│   │   └── LlmGateway.java
│   │
│   ├── memory
│   │   ├── ConversationMemoryService.java
│   │   └── PostgresChatMemoryStore.java
│   │
│   ├── rag
│   │   └── RagService.java
│   │
│   └── tool
│       ├── OrderToolService.java
│       ├── SupportTicketToolService.java
│       └── SupportToolService.java
│
├── controller
│
├── dto
│
├── entity
│
├── repository
│
├── security
│
├── service
│
└── enums
```

---

# 🛠️ Technology Stack

## Backend

* Java 24
* Spring Boot 4.1.1
* Spring Web
* Spring Data JPA
* Hibernate
* Spring Security

## Database

* PostgreSQL
* Neon PostgreSQL
* pgvector
* HNSW vector index

## AI / GenAI

* Google Gemini
* Spring AI
* LangChain4j
* Google GenAI
* RAG
* Semantic Search
* Hybrid Search
* AI Tool Calling

## Resilience

* Resilience4j
* Circuit Breaker
* LLM Response Caching

## Authentication

* JWT
* Spring Security
* Role-Based Access Control

## Development

* Maven
* Lombok
* JUnit
* Spring Boot Actuator

---

# ⚙️ Configuration

Required environment variable:

```text
GEMINI_API_KEY=<your-gemini-api-key>
```

Example configuration:

```properties
spring.ai.google.genai.embedding.api-key=${GEMINI_API_KEY}

spring.ai.google.genai.embedding.text.model=text-embedding-004

spring.ai.vectorstore.pgvector.initialize-schema=true
spring.ai.vectorstore.pgvector.index-type=HNSW
spring.ai.vectorstore.pgvector.distance-type=COSINE_DISTANCE
spring.ai.vectorstore.pgvector.dimensions=768
```

Database configuration should be supplied through environment variables rather
than committed to source control.

---

# 🚀 Running the Application

## 1. Clone the repository

```bash
git clone <repository-url>
cd ai-support-copilot
```

## 2. Configure PostgreSQL

Create a PostgreSQL database and enable pgvector:

```sql
CREATE EXTENSION IF NOT EXISTS vector;
```

## 3. Configure Gemini API Key

Set:

```text
GEMINI_API_KEY
```

as an environment variable.

## 4. Run the application

Using Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

---

# 🔌 Example API Endpoints

## AI Test

```http
GET /api/ai/test?message=Hello
```

---

## RAG Query

```http
GET /api/rag/ask?question=What is the return policy?
```

---

## Knowledge Search

```http
GET /api/search?keyword=refund
```

---

## Semantic Search

```http
GET /api/search/semantic?query=Can I return my product?
```

---

## Hybrid Search

```http
GET /api/search/hybrid?query=Can I return my product?
```

---

## Agent Chat

```http
POST /api/agent/chat
Authorization: Bearer <JWT>
Content-Type: application/json
```

Request:

```json
{
  "conversationId": null,
  "message": "Where is my order?"
}
```

Response:

```json
{
  "conversationId": "conversation-uuid",
  "response": "Please provide your order ID so I can check it."
}
```

---

## Continue Conversation

```json
{
  "conversationId": "conversation-uuid",
  "message": "My order ID is 44444444-4444-4444-4444-444444444444"
}
```

---

## Close Conversation

```http
POST /api/agent/conversations/{conversationId}/close
Authorization: Bearer <JWT>
```

---

# 📊 Current Implementation

| Feature                    | Status |
| -------------------------- | ------ |
| Spring Boot REST Backend   | ✅      |
| PostgreSQL                 | ✅      |
| JPA / Hibernate            | ✅      |
| JWT Authentication         | ✅      |
| Role-Based Authorization   | ✅      |
| LLM Gateway                | ✅      |
| Google Gemini              | ✅      |
| LLM Caching                | ✅      |
| Circuit Breaker            | ✅      |
| Document Ingestion         | ✅      |
| Document Chunking          | ✅      |
| pgvector                   | ✅      |
| Semantic Search            | ✅      |
| Keyword Search             | ✅      |
| Hybrid Search              | ✅      |
| RAG                        | ✅      |
| LangChain4j Agent          | ✅      |
| AI Tool Calling            | ✅      |
| Order Tool                 | ✅      |
| Support Ticket Tool        | ✅      |
| Conversation History       | ✅      |
| Conversation Lifecycle     | ✅      |
| Conversation Memory        | ✅      |
| Persistent Chat Memory     | ✅      |
| Memory Eviction            | ✅      |
| Agent Routing Improvements | 🔄     |
| Additional Business Tools  | 🔄     |
| Structured AI Responses    | 🔄     |
| Streaming / SSE            | 🔄     |
| Comprehensive Testing      | 🔄     |
| Observability              | 🔄     |
| Production Hardening       | 🔄     |

---

# 🎯 Future Enhancements

Planned improvements include:

### Intelligent Agent Routing

Improve routing between:

```text
Normal Chat
    |
    +---- RAG
    |
    +---- Order Tools
    |
    +---- Support Ticket
```

### Additional Tools

Potential tools:

* Get customer orders
* Get product details
* Check inventory
* Get order details
* Cancel order
* Return order

### Streaming

Add Server-Sent Events (SSE) for real-time token streaming:

```text
Gemini
   |
   v
Spring Boot
   |
   v
SSE
   |
   v
Frontend
```

### Observability

Add:

* Spring Boot Actuator
* Micrometer
* Prometheus
* OpenTelemetry
* Structured logging
* LLM request/response metrics

### Testing

Add:

* Unit tests
* Integration tests
* Repository tests
* Security tests
* RAG tests
* Agent/tool tests
* PostgreSQL Testcontainers

---

# 📈 Engineering Highlights

* Built a modular **AI + backend architecture** rather than tightly coupling
  LLM logic with REST controllers.
* Combined **traditional backend business logic with AI agent capabilities**.
* Implemented both **keyword and semantic retrieval** for improved knowledge
  discovery.
* Used **pgvector + HNSW** for scalable vector similarity search.
* Added **LLM resilience patterns** using circuit breakers and caching.
* Enforced authorization inside backend tools instead of trusting AI-generated
  decisions.
* Separated **permanent conversation history** from **LLM working memory**.
* Designed conversation memory around a unique `conversationId` to isolate
  customer sessions.

---

# 👨‍💻 Author

**Sivaji**

Java Backend Developer | Spring Boot | Generative AI | Distributed Systems

```

### One change I'd recommend

Since this README will live in your GitHub repository, I would **not claim the future features as completed**. Keep the current implementation and future roadmap clearly separated.

Your strongest project story is:

> **Java/Spring Boot backend + secure business APIs + Gemini + RAG + pgvector + LangChain4j agents + persistent conversational memory.**

That makes the project useful for both **Java Backend** and **GenAI/AI Backend** positions.
```
