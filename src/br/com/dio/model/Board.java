package br.com.dio.model;

import java.util.Collection;
import java.util.List;

import static br.com.dio.model.GameStatusEnum.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Space>> spaces;

    public Board(List<List<Space>> space) {
        this.spaces = space;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    // Checks if board has started, is complete or incomplete
    public GameStatusEnum getStatus() {
        if (spaces.stream()
            .flatMap(Collection::stream)
            .noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))) {
            return NON_STARTED;
        }

        return spaces.stream()
                .flatMap(Collection::stream)
                .anyMatch(s -> isNull(s.getActual())) ? INCOMPLETE : COMPLETE;
    }

    // Check if any user input numbers are incorrect
    public boolean hasErrors() {
        if (getStatus() == NON_STARTED) {
            return false;
        }

        return spaces.stream()
                .flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getActual()) && !s.getActual().equals(s.getExpected()));
    }

    // Changes value at a given coordinate (except for fixed numbers)
    public boolean changeValue(final int col, final int row, final int value) {
        var space = spaces.get(col).get(row);
        if (space.isFixed()) {
            return false;
        }

        space.setActual(value);
        return true;
    }

    // Clears value at a given coordinate (except for fixed numbers)
    public boolean clearValue(final int col, final int row) {
        var space = spaces.get(col).get(row);
        if (space.isFixed()) {
            return false;
        }

        space.clearSpace();
        return true;
    }

    // Resets board
    public void reset() {
        spaces.forEach(c -> c.forEach(Space::clearSpace));
    }

    // Checks if game is finished
    public boolean gameIsFinished() {
        return !hasErrors() && getStatus() == COMPLETE;
    }

}
