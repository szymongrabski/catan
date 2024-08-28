package com.example.backend.gameDetails.board;

import com.example.backend.gameDetails.board.Hex.Hex;
import com.example.backend.gameDetails.board.Hex.HexNumber;
import com.example.backend.gameDetails.board.Hex.HexService;
import com.example.backend.gameDetails.board.Hex.HexType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final HexService hexService;

    @Autowired
    public BoardService(BoardRepository boardRepository, HexService hexService) {
        this.boardRepository = boardRepository;
        this.hexService = hexService;
    }

    public Board createBoard() {
        Board board = new Board();
        initializeBoard(board);
        return boardRepository.save(board);
    }

    private void initializeBoard(Board board) {
        int[] rowSizes = {3, 4, 5, 4, 3};
        int numRows = rowSizes.length;

        for (int r = 0; r < numRows; r++) {
            int rowSize = rowSizes[r];
            int startQ = r == 0 ? 0 : r == 1 ? -1 : -2;

            for (int q = 0; q < rowSize; q++) {
                int actualQ = startQ + q;
                HexType type = getNextHexType(board);
                HexNumber number = (type == HexType.EMPTY) ? HexNumber.ZERO : getNextHexNumber(board);
                Hex hex = hexService.createHex(actualQ, r - 2, type, number);
                board.getHexes().add(hex);
            }
        }
    }

    private HexType getNextHexType(Board board) {
        HexType[] hexTypes = HexType.values();
        HexType selectedType;

        do {
            selectedType = hexTypes[board.random.nextInt(hexTypes.length)];
        } while (board.hexTypeCounts.getOrDefault(selectedType, 0) >= board.hexTypeLimits.getOrDefault(selectedType, 0));

        board.hexTypeCounts.put(selectedType, board.hexTypeCounts.getOrDefault(selectedType, 0) + 1);
        return selectedType;
    }

    private HexNumber getNextHexNumber(Board board) {
        HexNumber[] hexNumbers = HexNumber.values();
        HexNumber selectedNumber;

        do {
            selectedNumber = hexNumbers[board.random.nextInt(hexNumbers.length)];
        } while (board.hexNumberCounts.getOrDefault(selectedNumber, 0) >= board.hexNumberLimits.getOrDefault(selectedNumber, 0));

        board.hexNumberCounts.put(selectedNumber, board.hexNumberCounts.getOrDefault(selectedNumber, 0) + 1);
        return selectedNumber;
    }

    public Board getBoard(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }
}
