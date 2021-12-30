package com.qoomon.banking.swift.bcsmessage;

import com.qoomon.banking.swift.bcsmessage.BCSMessage;
import com.qoomon.banking.swift.bcsmessage.BCSMessageParseException;
import com.qoomon.banking.swift.bcsmessage.BCSMessageParser;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by qoomon on 25/07/16.
 */
public class BCSMessageParserTest {

    @Test
    public void parse_SHOULD_parse_palid_message() throws Exception {
        // Given
        String messageText = "835?20foo?36bar";

        BCSMessageParser subjectUnderTest = new BCSMessageParser();

        // When
        BCSMessage message = subjectUnderTest.parseMessage(messageText);

        // Then
        assertThat(message).isNotNull();
        assertThat(message.getBusinessTransactionCode()).isEqualTo("835");
        assertThat(message.getFieldMap())
                .containsEntry("20", "foo")
                .containsEntry("36", "bar")
                .hasSize(2);
    }


    @Test
    public void parse_SHOULD_accept_any_delimiter() throws Exception {
        // Given
        String messageText = "835/20foo/36bar";

        BCSMessageParser subjectUnderTest = new BCSMessageParser();

        // When
        BCSMessage message = subjectUnderTest.parseMessage(messageText);

        // Then
        assertThat(message).isNotNull();
        assertThat(message.getBusinessTransactionCode()).isEqualTo("835");
        assertThat(message.getFieldMap())
                .containsEntry("20", "foo")
                .containsEntry("36", "bar")
                .hasSize(2);
    }

    @Test
    public void parse_THROW_on_duplicate_fields() throws Exception {
        // Given
        String messageText = "835?20foo?20bar";

        BCSMessageParser subjectUnderTest = new BCSMessageParser();

        // When

        Throwable thrown = catchThrowable(() -> subjectUnderTest.parseMessage(messageText));

        // then
        assertThat(thrown).isInstanceOf(BCSMessageParseException.class)
                .hasMessageContaining("duplicate field " + "20");
    }

    @Test
    public void parse_multible_fields() throws Exception {
	 // Given
	    String messageText = "106?00Kartenzahlung girocard?10931?20EREF+7001472926604611122100" +
	    		             "?211240?22KREF+2021121360272010587700?23000000008336" +
	    		             "?24MREF+OFFLINE?25CRED+SE917375565480026?26PURP+IDCP" +
	    		             "?27SVWZ+SHELL 0516/ Lauf/DE   ?28     11.12.2021 um 00:12:40" +
	    		             "?29 Uhr 70014729/266046/ECTL/ ?30CITIDEFFXXX" +
	    		             "?31DE67502109000218391443?32Worldline Sweden AB fuer Sh?33ell" +
	    		             "?34011?60     76060618/0000201626/0/?611223";

	    
	    
        BCSMessageParser subjectUnderTest = new BCSMessageParser();

        // When
        BCSMessage message = subjectUnderTest.parseMessage(messageText);

        // Then
        assertThat(message).isNotNull();
        assertThat(message.getBusinessTransactionCode()).isEqualTo("106");
        assertThat(message.getFieldMap())
                .containsEntry("20", "EREF+7001472926604611122100")
                .containsEntry("28", "     11.12.2021 um 00:12:40")
                .containsEntry("31", "DE67502109000218391443")
                .hasSize(19);

    }
}