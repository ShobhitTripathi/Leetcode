You need a small set of reusable, high-signal prompts that you can type in 15–30 seconds and adapt to the situation.

I'd recommend the following 6 compact prompts.

## 1. Understand the codebase

```
Analyze this repository. Identify its architecture, entry points,
key components, coding conventions, and relevant test structure.
Focus only on files relevant to the given problem.
Do not modify anything yet.
```

## 2. Investigate the issue

```
Investigate this issue: [PROBLEM].

Trace the execution flow, identify the root cause using actual
code, and explain why it happens.

Check edge cases and potential side effects.
Do not modify code yet.
```

## 3. Plan the solution

```
Propose the simplest production-ready fix.

Identify impacted files, implementation changes, edge cases,
and required tests.

Follow existing repository conventions. Avoid unnecessary
refactoring and preserve backward compatibility.
```

## 4. Implement the solution

```
Implement the proposed fix.

Follow existing patterns, handle errors and edge cases,
and minimize changes to unrelated code.

Add appropriate unit tests. Do not assume correctness
without running the tests.
```

## 5. Validate and review

```
Review the changes independently.

Check functional correctness, edge cases, error handling,
concurrency, performance, security, and regressions.

Run relevant tests, build, lint, and static analysis.
Fix issues found and report actual validation results.
```

## 6. Final conformance check

```
Compare the final implementation against the original requirements.

Verify every requirement is implemented and tested.
Inspect the complete Git diff for unintended changes.

Report PASS/FAIL for each requirement, remaining issues,
and whether the solution is ready for submission.
```

## How I'd use these in a 60-minute interview

## 60-minute interview strategy

0–5 min

Understand

Read the problem, inspect the repository and identify relevant components.

5–12 min

Investigate

Use AI to identify root cause and clarify requirements.

12–17 min

Plan

Agree on the minimal implementation approach.

17–35 min

Implement

Make changes and add targeted tests.

35–50 min

Validate

Run tests, inspect failures, review the diff and fix defects.

50–60 min

Conformance

Verify requirements, explain trade-offs and present the solution.

## 3 additional prompts worth remembering

These are useful when something unexpected happens.

When AI makes a questionable change:

```
Challenge your proposed solution. Identify alternative approaches,
hidden assumptions, and potential failure scenarios. Recommend
the safest approach.
```

When tests fail:

```
Analyze the test failures. Determine whether they indicate a
product defect, test defect, or environment issue. Fix the root
cause without weakening existing tests.
```

When AI changes too many files:

```
Review the diff and eliminate unnecessary changes.
Keep only modifications essential to the requirement.
Preserve existing behavior.
```

## My strongest recommendation

Don't try to memorize all nine prompts.

Remember this sequence:

# Understand → Investigate → Plan → Implement → Validate → Conform

Your reusable AI engineering workflow

One more thing: In a senior engineer interview, don't blindly accept AI output. Ask it to explain its decisions, challenge its assumptions, and independently verify its changes.

That engineering judgment is likely to matter more than prompt length.
