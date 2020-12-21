#include <stdio.h>
#include <stdlib.h>

struct Node
{
    int value;
    struct Node *previous;
    struct Node *next;
} typedef Node;

Node* makeNewNode(int newValue) {
    Node *newNode = (struct Node *)malloc(sizeof(struct Node));
    newNode->value = newValue;
    newNode->previous = NULL;
    newNode-> next = NULL;
    return newNode;
}

void printList(Node *head) {
    while (head != NULL) {
        printf("%d\n", head->value);
        head = head->next;
    }
}

Node* insert(int newValue, Node *head)
{
    Node *newNode = makeNewNode(newValue);

    if (newValue <= head->value) {
        // Insert at beginning of list.
        newNode->next = head;
        head->previous = newNode;
        // Return the new head.
        return newNode;
    }

    Node *previous = head;
    Node *next = head->next;
    while (next != NULL && next->value < newValue) {
        previous = next;
        next = next->next;
    }

    if (next == NULL) {
        // Add to end of list.
        previous->next = newNode;
        newNode->previous = previous;
    }
    else {
        // Insert into list.
        previous->next = newNode;
        newNode->previous = previous;
        next->previous = newNode;
        newNode->next = next;
    }

    // Head didn't change.
    return head;
}

int main() {
    Node *head = (struct Node *)malloc(sizeof(struct Node));

    head->value = 8;
    head->previous = NULL;
    head->next = NULL;

    head = insert(3, head);
    head = insert(20, head);
    head = insert(9, head);
    head = insert(1, head);
    head = insert(5000, head);
    head = insert(9, head);

    printList(head);
}