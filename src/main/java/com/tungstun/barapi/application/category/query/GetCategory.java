package com.tungstun.barapi.application.category.query;

import java.util.UUID;

public record GetCategory(
        UUID categoryId,
        UUID barId) {
}
